package org.ccframe.subsys.core.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeRootEnum;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.data.BatchImportSupport;
import org.ccframe.commons.data.ExcelReaderError;
import org.ccframe.commons.data.ImportDataCheckUtil;
import org.ccframe.commons.data.ListExcelReader;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.jpaquery.Criteria;
import org.ccframe.commons.jpaquery.Restrictions;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.AppConstant;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.ValidateCodeStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.ccframe.subsys.core.domain.entity.MenuResUserHit;
import org.ccframe.subsys.core.domain.entity.OrgUserRel;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.i18n.client.NumberFormat;


@Service
public class UserService extends BaseService<User, Integer, UserRepository> implements BatchImportSupport<User>{

	private static final String USER_IMPORT_TEMPLATE_FILE_NAME = "userImport.xls";
	private static Map<String, String> validateCodeMap = new HashMap<String, String>();
	private static Timer timer = new Timer();
	private Map<String, Double> importStatusMap = new ConcurrentHashMap<String, Double>();
	
	/**
	 * 按登录ID获取
	 * @param loginId
	 * @return
	 */
	@Transactional(readOnly = true)
	public User getByLoginId(String loginId){
		return this.getByKey(User.LOGIN_ID, loginId);
	}
	
	
	/**
	 * 前后台都可用的综合登录逻辑.如果LOGIN方式无法登录，则会采用手机或EMAIL的方式去登录.
	 * @param loginId
	 * @param userPsw
	 * @return
	 */
	@Transactional(readOnly = true)
	public User getByMultiLoginIdUserPswOrgId(String multiLoginId, String userPsw, Integer orgId){
		
		User user = SpringContextHelper.getBean(UserSearchService.class).getByMultiLoginIdUserPsw(multiLoginId, userPsw);
		if(user == null){
			throw new BusinessException(ResGlobal.ERRORS_LOGIN_PASSWORD, true);
		}else{
			if(UserStatCodeEnum.fromCode(user.getUserStatCode()) == UserStatCodeEnum.FREEZE ){ //用户冻结逻辑
				throw new BusinessException(ResGlobal.ERRORS_LOGIN_FREEZE, new String[]{multiLoginId});
			}
		}
//医疗系统允许自由登录所有的校区
/*		List<OrgUserRel> orgUserRelList = SpringContextHelper.getBean(OrgUserRelSearchService.class).findByKey(OrgUserRel.USER_ID, user.getUserId());
		boolean userNotInOrg = true;
		for(OrgUserRel orgUserRel: orgUserRelList){
			if(orgUserRel.getOrgId().equals(orgId)){
				userNotInOrg = false;
				break;
			}
		}
		if(userNotInOrg){
			throw new BusinessException(ResGlobal.ERRORS_LOGIN_PASSWORD, true);
		}
*/		return user;
	}

	/**
	 * 通常情况下，用户的LoinId是不会修改的，以下两个情况除外：
	 * 1. 当LoginId与用户手机相同时. 说明用户是纯手机注册,修改手机号码将同时修改LoginId为新手机
	 * 2. 当LoginId与用户邮箱相同时. 说明用户是纯邮箱注册,修改用户邮箱将同时修改LoginId为新邮箱
	 * 
	 * @see org.ccframe.commons.base.BaseService#update(java.lang.Object)
	 */
	@Transactional
	public void update(User data){
		User oldUser = getById(data.getUserId());
		if(oldUser.getLoginId().equals(oldUser.getUserMobile())){
			data.setLoginId(data.getUserMobile());
		}
		if(oldUser.getLoginId().equals(oldUser.getUserEmail())){
			data.setLoginId(data.getUserEmail());
		}
		SpringContextHelper.getBean(this.getClass()).save(data);
	}

//	@Transactional(readOnly = true)
//	public ClientPage<UserRowDto> findUserList(UserListReq userListReq, int offset, int limit) {
//		//TODO搜索用solr
//		Page<User> userPage = getRepository().findAll(
//			new Criteria<User>()
//			.add(Restrictions.eq(User.LOGIN_ID, userListReq.getLoginId()))
//			.add(Restrictions.like(User.USER_NM, userListReq.getUserNm()))
//			.add(Restrictions.gt(User.CREATE_DATE, userListReq.getCreateDateStart()))
//			.add(Restrictions.lt(User.CREATE_DATE, userListReq.getCreateDateEnd()))
//			.add(Restrictions.in(
//				User.USER_STAT_CODE, 
//				StringUtils.isBlank(userListReq.getUserStatCode()) ? 
//					new String[]{UserStatCodeEnum.NORMAL.toCode(), UserStatCodeEnum.FREEZE.toCode()} :
//					new String[]{userListReq.getUserStatCode()}
//			)), 
//			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, User.CREATE_DATE))
//		);
//				
//		List<UserRowDto> resultList = new ArrayList<UserRowDto>();
//		for(User user:userPage.getContent()){
//			UserRowDto rowRecord = new UserRowDto();
//			BeanUtils.copyProperties(user, rowRecord);
//			user.setUserPsw("");//列表返回屏蔽用户密码
//			//TODO 用户账户信息
//			resultList.add(rowRecord);
//		}
//		return new ClientPage<UserRowDto>((int)userPage.getTotalElements(), offset, limit, resultList);
//	}

	/**
	 * 用户软删除.
	 * 检查登录ID、电话或邮箱字段，如果有则尝试添加@Del+序号，序号用来解决添加时的唯一性冲突.
	 * 
	 * 局限性是只能允许一个用户反复添加99次，注意自动测试的影响.
	 * @param userId
	 */
	@Transactional
	public void softDeleteById(int userId) {
		if(userId == Global.ADMIN_USER_ID){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"默认管理员禁止删除"});
		}
		User user = getById(userId);
		user.setUserStatCode(UserStatCodeEnum.DELETED.toCode());

		Page<User> checkLoginIdUserPage = this.getRepository().findAll(new Criteria<User>().add(Restrictions.like(User.LOGIN_ID, user.getLoginId() + Global.DELETE_DATA_KEY + "%")),
			new PageRequest(0, 1, new Sort(new Order(Direction.DESC, User.LOGIN_ID)))
		);

		User checkLoginIdUser = checkLoginIdUserPage.getContent().isEmpty() ? null : checkLoginIdUserPage.getContent().get(0);
		if(checkLoginIdUser == null){
			user.setLoginId(user.getLoginId() + Global.DELETE_DATA_KEY + "01");
		}else{
			user.setLoginId(calcDelField(checkLoginIdUser.getLoginId(), user.getLoginId()));
		}

		if(StringUtils.isNotEmpty(user.getUserMobile())){
			Page<User> checkUserMobilePage = this.getRepository().findAll(new Criteria<User>().add(Restrictions.like(User.USER_MOBILE, user.getUserMobile() + Global.DELETE_DATA_KEY + "%")),
				new PageRequest(0, 1, new Sort(new Order(Direction.DESC, User.USER_MOBILE)))
			);

			User checkUserMobileUser = checkUserMobilePage.getContent().isEmpty() ? null : checkUserMobilePage.getContent().get(0);
			if(checkUserMobileUser == null){
				user.setUserMobile(user.getUserMobile() + Global.DELETE_DATA_KEY + "01");
			}else{
				user.setUserMobile(calcDelField(checkUserMobileUser.getUserMobile(), user.getUserMobile()));
			}
		}

		if(StringUtils.isNotEmpty(user.getUserEmail())){
			Page<User> checkUserEmailPage = this.getRepository().findAll(new Criteria<User>().add(Restrictions.like(User.USER_EMAIL, user.getUserEmail() + Global.DELETE_DATA_KEY + "%")),
				new PageRequest(0, 1, new Sort(new Order(Direction.DESC, User.USER_EMAIL)))
			);
			User checkUserEmailUser = checkUserEmailPage.getContent().isEmpty() ? null : checkUserEmailPage.getContent().get(0);
			if(checkUserEmailUser == null){
				user.setUserEmail(user.getUserEmail() + Global.DELETE_DATA_KEY + "01");
			}else{
				user.setUserEmail(calcDelField(checkUserEmailUser.getUserEmail(), user.getUserEmail()));
			}
		}

		update(user);
	}

	private String calcDelField(String lastDelData, String fieldData){
		NumberFormat fmt = NumberFormat.getFormat("00");
		int indexNum = Math.getExponent(fmt.parse(lastDelData.substring(fieldData.length() + Global.DELETE_DATA_KEY.length())));
		return fieldData + Global.DELETE_DATA_KEY + fmt.format(indexNum + 1);
	}
	
	@Transactional
	public void updatePassword(Integer userId, String userPsw) {
		User user = this.getById(((AdminUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN)).getUserId());

		if(userId == null){ //判断是否更改自己密码，如果更改自己密码不用判断授权
			user.setUserPsw(userPsw);
		}else{ //修改别人密码，必须具备修改密码权限才可操作(用户是admin) TODO:权限判断要细化到功能按钮
			if(!user.getUserId().equals(Global.ADMIN_USER_ID)){
				throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"您无权操作此功能！"});
			}
			user = getById(userId);
		}
		update(user);
	}

	@Override
	@Transactional
	public User save(User adminUser) {
		if(adminUser.getUserId() == null){
			adminUser.setCreateDate(new Date());
		}
		User result = super.save(adminUser);

		//TODO 以后要添加“管理员管理”管理机构管理员用户和分配权限，本阶段先认为所有保存的管理员都是总部的。更改逻辑后，所有的会员保存逻辑也要改，会员只能看到管理员，但是不能删除管理员和修改是否管理员。
		OrgUserRelService orgUserRelService = SpringContextHelper.getBean(OrgUserRelService.class);
		List<OrgUserRel> orgUserRelList = orgUserRelService.findByKey(UserRoleRel.USER_ID, adminUser.getUserId());
		if(BoolCodeEnum.fromCode(adminUser.getIfAdmin()).boolValue()){ //是机构管理员，如果不是则要添加
			if(orgUserRelList.isEmpty()){
				OrgUserRel orgUserRel = new OrgUserRel();
				orgUserRel.setUserId(adminUser.getUserId());
				orgUserRel.setOrgId(Global.PLATFORM_ORG_ID);
				orgUserRelService.save(orgUserRel);
			}
		}else{ //不是机构管理员，如果有机构关联则要删除
			if(!orgUserRelList.isEmpty()){
				for(OrgUserRel orgUserRel: orgUserRelList){
					orgUserRelService.delete(orgUserRel);
				}
			}
		}
		return result;
	}

	@Transactional
	public void saveOrUpdateAdminUser(User user) {
		//为了避免攻击，该操作禁止修改用户密码 TODO:权限判断要细化到功能按钮
		if(user.getUserId() != null){
			User oldUser = getById(user.getUserId());
			user.setUserPsw(oldUser.getUserPsw());
		}
		SpringContextHelper.getBean(this.getClass()).save(user);
	}

	
	private List<ExcelReaderError> dataLogicCheck(User checkValue,int rowNum){
		List<ExcelReaderError> resultList = new ArrayList<>();
		ImportDataCheckUtil.stringCheck("登录名", 38 - 6, false, checkValue.getLoginId(), rowNum, 0, resultList);
		ImportDataCheckUtil.stringCheck("用户名称", 32, false, checkValue.getUserNm(), rowNum, 1, resultList);
		ImportDataCheckUtil.patternCheck("用户手机", Global.MOBILE_PATTERN, true, checkValue.getUserMobile(), rowNum, 2, resultList);
		ImportDataCheckUtil.patternCheck("用户E-MAIL", Global.EMAIL_PATTERN, true, checkValue.getUserEmail(), rowNum, 3, resultList);
		ImportDataCheckUtil.enumCheck("是否管理员", "是,否", checkValue.getIfAdmin(), rowNum, 4, resultList);
		return resultList;
	}
	
	@Override
	@Transactional
	public List<ExcelReaderError> importBatch(int rowBase, List<User> importLit, boolean isLastRow, Map<String, Object> importParam) {
		List<ExcelReaderError> resultList = new ArrayList<ExcelReaderError>();
		int rowNum = rowBase;
		for(User rowUser: importLit){
			List<ExcelReaderError> checkErrorList = dataLogicCheck(rowUser, rowNum);
			rowNum ++;
			if(checkErrorList.size() > 0){ //有错误，不处理
				resultList.addAll(checkErrorList);
				continue;
			}
			
			User loginUser = this.getByKey(User.LOGIN_ID, rowUser.getLoginId());
			User mobileUser = null;
			if(StringUtils.isNotBlank(rowUser.getUserMobile())){
				mobileUser = this.getByKey(User.USER_MOBILE, rowUser.getUserMobile());
			}
			User emailUser = null;
			if(StringUtils.isNotBlank(rowUser.getUserEmail())){
				emailUser = this.getByKey(User.USER_EMAIL, rowUser.getUserEmail());
			}
			if(loginUser != null){ //add
				if(mobileUser != null){
					resultList.add(new ExcelReaderError(2, rowNum - 1, "用户手机重复"));
					continue;
				}
				if(emailUser != null){
					resultList.add(new ExcelReaderError(3, rowNum - 1, "用户E-MAIL重复"));
					continue;
				}
			}else{ //update
				if(mobileUser != null && !mobileUser.equals(loginUser)){
					resultList.add(new ExcelReaderError(2, rowNum - 1, "用户手机重复"));
					continue;
				}
				if(emailUser != null && !mobileUser.equals(emailUser)){
					resultList.add(new ExcelReaderError(3, rowNum - 1, "用户E-MAIL重复"));
					continue;
				}
			}
			
			User dbUser = this.getByKey(User.LOGIN_ID, rowUser.getLoginId());
			if(dbUser == null){ //add
				dbUser = new User();
				dbUser.setLoginId(rowUser.getLoginId());
				dbUser.setUserPsw(DigestUtils.sha512Hex("123456"));
				dbUser.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
			}
			dbUser.setUserNm(rowUser.getUserNm());
			dbUser.setUserMobile(rowUser.getUserMobile());
			dbUser.setUserEmail(rowUser.getUserEmail());
			dbUser.setIfAdmin(BoolCodeEnum.toCode("是".equals(rowUser.getIfAdmin())));
			dbUser.setCreateDate(rowUser.getCreateDate());
			SpringContextHelper.getBean(this.getClass()).save(dbUser);
			getRepository().flush(); //临时刷新到数据库，用于判断重复
		}
//		try {
//			Thread.sleep(2000); //TODO 去掉，延迟观察进度条
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		return resultList;
	}

	@Async
	public void doImport(String filePath, Map<String, Object> importParam) { //批量导入进行事务控制，因此无需要添加事务
		ListExcelReader<User> listExcelReader= new ListExcelReader<>(WebContextHolder.getWarPath() + File.separator + Global.EXCEL_EXPORT_TEMPLATE_DIR + File.separator + USER_IMPORT_TEMPLATE_FILE_NAME, User.class);
		listExcelReader.readFromFile(filePath, SpringContextHelper.getBean(UserService.class), importParam);
	}


	@Override
	public Map<String, Double> getImportStatusMap() {
		return importStatusMap;
	}


	/**
	 * 楚钦添加，因为‘诊断 处理 日志’表中的‘处理 记录’需要name这个字段
	 * 需要优化!!! //TODO 清理
	 * @param doctorId
	 * @return
	 */
	@Transactional(readOnly = true)
	public String getNameById(Integer userId) {
		User user = this.getById(userId);
		return user.getUserNm();
	}


	/**
	 * 查询某个角色下的用户.
	 * @param roleId
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<User> findByRoleId(Integer roleId) {
		List<UserRoleRel> userRoleRelList = SpringContextHelper.getBean(UserRoleRelService.class).findByKey(UserRoleRel.ROLE_ID, roleId);
		List<User> resultList = new ArrayList<User>();
		for(UserRoleRel userRoleRel: userRoleRelList){
			resultList.add(SpringContextHelper.getBean(UserSearchService.class).getById(userRoleRel.getUserId()));
		}
		return resultList;
	}

	private List<Integer> getUserRoleMenuList(Integer userId, Integer orgId) {
		List<Integer> roleIdListFromUser = new ArrayList<Integer>();
		for(UserRoleRel userRoleRel: SpringContextHelper.getBean(UserRoleRelSearchService.class).findByKey(UserRoleRel.USER_ID, userId)){
			roleIdListFromUser.add(userRoleRel.getRoleId());
		}
		List<Integer> roleIdListFromOrg = new ArrayList<Integer>();
		for(Role role: SpringContextHelper.getBean(RoleSearchService.class).findByKey(Role.ORG_ID, orgId)){
			roleIdListFromOrg.add(role.getRoleId());
		}
		Collection<Integer> roleIdList = CollectionUtils.intersection(roleIdListFromOrg, roleIdListFromUser);
		List<Integer> objectIdList = new ArrayList<Integer>();
		for(RoleMenuResRel roleMenuResRel: SpringContextHelper.getBean(RoleMenuResRelSearchService.class).findByRoleIdIn(roleIdList)){
			objectIdList.add(roleMenuResRel.getMenuResId());
		};
		return objectIdList;
	}
	
	/**
	 * 搜索用户角色在机构下的角色菜单树.
	 * @param userId
	 * @param orgId
	 * @return
	 */
	@Transactional(readOnly = true)
	public TreeNodeTree getUserRoleMenuTree(Integer userId, Integer orgId) {
		return SpringContextHelper.getBean(TreeNodeSearchService.class).getTree(TreeRootEnum.MENU_TREE_ROOT.getTreeNodeId(), getUserRoleMenuList(userId,orgId));
	}


	public List<MenuRes> findFastMenuRes(Integer userId, Integer orgId) {
		List<MenuRes> userMenuResList = new ArrayList<MenuRes>();
		MenuResService menuResService = SpringContextHelper.getBean(MenuResService.class);
		//获得有操作的菜单集合
		for(Integer menuResId: getUserRoleMenuList(userId,orgId)){
			MenuRes menuRes = menuResService.getById(menuResId);
			if(StringUtils.isNotBlank(menuRes.getResUrl())){
				userMenuResList.add(menuRes);
			}
		}
		if(userMenuResList.isEmpty()){ //没有任何菜单权限
			return userMenuResList;
		}
		List<MenuResUserHit> menuResUserHitList = SpringContextHelper.getBean(MenuResUserHitSearchService.class).findFastMenuRes(userId);
		//策略：选择最多4个在可用菜单列表的资源项，如果不足则使用角色资源来补足
		List<MenuRes> resultList = new ArrayList<MenuRes>();
		for(MenuResUserHit menuResUserHit: menuResUserHitList){
			MenuRes menuRes = menuResService.getById(menuResUserHit.getMenuResId());
			if(userMenuResList.contains(menuRes)){
				resultList.add(menuRes);
				if(resultList.size() == Global.FAST_MENU_TOOLS_NUM){
					return resultList;
				}
			}
		}
		//未满4个：补足菜单
		Collection<MenuRes> tmpList = CollectionUtils.subtract(userMenuResList, resultList);
		for(MenuRes menuRes: tmpList){
			resultList.add(menuRes);
			if(resultList.size() == Global.FAST_MENU_TOOLS_NUM){
				return resultList;
			}
		}
		//仍未足4个：用最后一个菜单补足
		MenuRes lastMenuRes = resultList.get(resultList.size() - 1);
		for(int i = resultList.size(); i < Global.FAST_MENU_TOOLS_NUM; i ++){
			resultList.add(lastMenuRes);
		}
		return resultList;
	}
	
	/**
	 * @author zjm
	 */
	@Transactional
	public void login(String phoneNumber, String iMEI, String validateCode, int orgId) {

		List<User> list = SpringContextHelper.getBean(UserSearchService.class).findByLoginIdAndUserPsw(phoneNumber, iMEI);
		User user = null;
		MemberUser memberUser = new MemberUser();
		if (list != null && list.size() > 0) {
			user = list.get(0);
			List<MemberAccount> listAccounts = SpringContextHelper.getBean(MemberAccountSearchService.class).findByUserIdAndOrgIdAndAccountTypeCode(user.getUserId(), orgId, AccountTypeCodeEnum.DEPOSIT.toCode());
			if (listAccounts == null || listAccounts.size() == 0) {
				this.createAccount(user, orgId);
			}
		} else {
			List<User> users = SpringContextHelper.getBean(UserService.class).findByKey(User.LOGIN_ID, phoneNumber);
			if (users != null && users.size() > 0) {
				// 更新用户的UserPsw(IMEI).
				user = users.get(0);
				user.setUserPsw(iMEI);
			} else {
				//新建用户
				user = new User();
				user.setLoginId(phoneNumber);
				user.setUserNm(phoneNumber);
				user.setUserMobile(phoneNumber);
				user.setUserPsw(iMEI);
				user.setIfAdmin(AppConstant.NO);
				user.setCreateDate(new Date());
				user.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
			}
			user = SpringContextHelper.getBean(UserService.class).save(user);
			this.createAccount(user, orgId);
		}
		memberUser = new MemberUser();
		memberUser.setUserId(user.getUserId());
		memberUser.setOrgId(orgId);
		WebContextHolder.getSessionContextStore().setServerValue(Global.SESSION_LOGIN_MEMBER_USER, memberUser);
	}
	
	/**
	 * 新建用户的押金、预存款和积分账户
	 * @author zjm
	 */
	public void createAccount(User user, int orgId) {
		//新建用户积分账户
		MemberAccount memberAccount = new MemberAccount();
		memberAccount.setUserId(user.getUserId());
		memberAccount.setOrgId(orgId);
		memberAccount.setAccountTypeCode(AccountTypeCodeEnum.INTEGRAL.toCode());
		memberAccount.setAccountValue(0.00);
		SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount);
		//新建用户预存款账户
		MemberAccount memberAccount2 = new MemberAccount();
		memberAccount2.setUserId(user.getUserId());
		memberAccount2.setOrgId(orgId);
		memberAccount2.setAccountTypeCode(AccountTypeCodeEnum.PRE_DEPOSIT.toCode());
		memberAccount2.setAccountValue(0.00);
		SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount2);
		//新建用户押金账户
		MemberAccount memberAccount3 = new MemberAccount();
		memberAccount3.setUserId(user.getUserId());
		memberAccount3.setOrgId(orgId);
		memberAccount3.setAccountTypeCode(AccountTypeCodeEnum.DEPOSIT.toCode());
		memberAccount3.setAccountValue(0.00);
		SpringContextHelper.getBean(MemberAccountService.class).save(memberAccount3);
	}

	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public String checkState(HttpServletRequest httpRequest) {
	
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		String state = "";
		List<CyclingOrder> list = SpringContextHelper.getBean(CyclingOrderSearchService.class).findByUserIdAndOrgIdOrderByStartTimeDesc(user.getUserId(), user.getOrgId());
		if(list!=null && list.size()>0) {
			if(list.get(0).getCyclingOrderStatCode().equals(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode())) {
				return AppConstant.ON_THE_WAY;
			} else if(list.get(0).getCyclingOrderStatCode().equals(CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode())) {
				return AppConstant.WAIT_PAY;
			}
		}
		 String flag = httpRequest.getParameter(AppConstant.FLAG);
		 if(flag!=null && flag.equals(AppConstant.ON_CREATE)) {
			 return AppConstant.FIRST;
			 //return "";
		 }
		
		return state;
	}
	
	/**
	 * @author lqz
	 * update by lzh
	 */
	public static String getValidateCode(final String loginId) {
		String validateCode = Double.toString(Math.random()).substring(2, 8);
		validateCodeMap.put(loginId, validateCode);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				validateCodeMap.remove(loginId);
			}
		}, 60 * 1000); //测试时间 10秒
		return validateCode;
	}

	public static ValidateCodeStatCodeEnum Validate(String loginId, String validateCode) {
//		return true;
		String code = validateCodeMap.get(loginId);
		if(code == null){
			return ValidateCodeStatCodeEnum.TIMEOUT;
		} else if(code.equals(validateCode)){
			return ValidateCodeStatCodeEnum.PASS;
		} else {
			return ValidateCodeStatCodeEnum.ERROR;
		}
	}

}