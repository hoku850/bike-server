package org.ccframe.commons.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.ccframe.commons.base.IHasSearchBuilder;
import org.ccframe.commons.util.DbUnitUtils;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.core.service.ParamService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;


/**
 * 系统初始化.
 *
 * @author wj
 */
@Component
public class SysInitBeanHelper implements InitializingBean{

	private DataSource dataSource;

	private String productName;
	
	private String initMode;
	
    private String defaultDataPath;
    
    private List<String> productDataPaths;

    private String loadAndDeleteDataPath;

    private String schema;

    private DbUnitUtils.DBTYPE dbType = DbUnitUtils.DBTYPE.MYSQL;

    private Map<String, String> replacementMap;
    
    private Map<String, String> taoColorMap;

	private Logger log = Logger.getLogger(this.getClass());

	private String defaultDeleteDataPath;

	private ParamService paramService;
	
	private boolean adminAutoLogin;
	
	public static final String APP_CONFIG_FILE_NAME = "/appConfig.local.properties";

    private static final String CREATE_ONCE = "create-once";

	public static final String INIT_MODE_PARAM_NAME = "app.initMode";

	public static boolean inited = false;
	
	@Value("${app.debug.adminAutoLogin:false}")
    public void setAdminAutoLogin(boolean adminAutoLogin) {
		this.adminAutoLogin = adminAutoLogin; 
	}
	
    public boolean isAdminAutoLogin() {
		return adminAutoLogin;
	}

    @Value("${app.productName:ccframe}")
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    @Value("${app.initMode:none}")
    public void setInitMode(String initMode){
    	String forceInitMode = System.getProperty("forceInitMode", null); //自动发布时，通过环境变量-D强制指定是重置还是不处理
    	this.initMode = (forceInitMode == null ? initMode : forceInitMode);
    }
    
    @Value("${app.dataSource.schema:}")
    public void setSchema(String schema){
    	this.schema = schema;
    }

    @Value("${app.init.defaultDataPath:}")
    public void setDefaultDataPath(String defaultDataPath) {
        this.defaultDataPath = defaultDataPath;
    }
    
    @Value("${app.init.productDataPath:}")
    public void setProductDataPath(String paths) {
        productDataPaths = new ArrayList<String>();
        if(StringUtils.isNotBlank(paths)){
            for (String productDataPath : paths.split(",")){
                if(StringUtils.isNotBlank(productDataPath)){
                    this.productDataPaths.add(productDataPath);
                }
            }
        }
    }

    @Value("${app.init.loadAndDeleteDataPath:}")
    public void setLoadAndDeleteDataPath(String loadAndDeleteDataPath){
    	this.loadAndDeleteDataPath = loadAndDeleteDataPath;
    }
    
    @Value("${app.init.defaultDeleteDataPath:}")
    public void setDefaultDeleteDataPath(String defaultDeleteDataPath){
    	this.defaultDeleteDataPath = defaultDeleteDataPath;
    }

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

    @Autowired
    public void setReplacementMap(Map<String, String> replacementMap){
    	this.replacementMap = replacementMap;
    }

    @Autowired
	public void setParamService(ParamService paramService) {
		this.paramService = paramService;
	}

    public static boolean isInited() {
		return inited;
	}

	@Override
    public void afterPropertiesSet(){
    	log.info(productName + "开始启动...");
    	registerConverter();
        createTables();
        loadOnceData();
        buildIndex();
        inited = true;
    	log.info(productName + "启动完毕...");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Class<? extends Annotation>> scanEntityClasses(String pattern) throws URISyntaxException, IOException, ClassNotFoundException{
    	List<Class<? extends Annotation>> annotatedClassList = new ArrayList<Class<? extends Annotation>>();
    	
    	int pathLen = WebContextHolder.getWarPath().length();
    	ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    	for(Resource resources: resolver.getResources(pattern)){ //"classpath:org/ccframe/subsys/*/domain/*.class"
    		String uriStr = resources.getFile().getPath();
    		Class scanClass = Class.forName(uriStr.substring(pathLen + 17, uriStr.length() - 6).replaceAll("/".equals(File.separator) ? "/" : "\\\\", "."));
			if(scanClass.getAnnotation(Entity.class) != null){
				annotatedClassList.add(scanClass);
			}
    	}
    	return annotatedClassList;
    }
    
    /**
     * 初始化系统表和业务表数据
     *
     * @throws java.sql.SQLException
     * @throws org.dbunit.DatabaseUnitException
     *
     * @throws Exception
     */
    private void createTables() {
        if ("create".equals(initMode) || CREATE_ONCE.equals(initMode)) { //TODO user hiberante constant instead of "create"
        	if(CREATE_ONCE.equals(initMode)){
        		log.info("整站数据安全创建模式，自动装入初始化数据...");
        	}else{
        		log.info("整站数据重新创建模式，自动装入初始化数据...");
        	}
            try {
            	List<String> tableNameList = new ArrayList<String>();
				for(Class<? extends Annotation> type: scanEntityClasses("classpath:org/ccframe/subsys/*/domain/entity/*.class")){
					tableNameList.add(type.getSimpleName());
				}

            	List<String> dataPaths = new ArrayList<String>();
            	if(productDataPaths != null){
                    for (String productDataPath : productDataPaths) {
                        dataPaths.add(productDataPath);
                    }
            	}
            	if(StringUtils.isNotBlank(defaultDataPath)){
            		dataPaths.add(defaultDataPath);
            	}
            	log.info("载入初始化数据...");
                Collections.reverse(dataPaths);
                DbUnitUtils.appendDbUnitData(dataSource, schema, dbType, replacementMap, dataPaths.toArray(new String[dataPaths.size()]));
                if(StringUtils.isNotBlank(defaultDeleteDataPath)){
                	DbUnitUtils.deleteDbUnitData(dataSource, schema, dbType, replacementMap, new String[]{defaultDeleteDataPath});
                }
            	log.info("自动装入初始化数据结束...");
            	if(CREATE_ONCE.equals(initMode)){
	            	log.info("启用安全创建模式，关闭数据重置标志...");
	            	closeConfigCreateMode();
	            	log.info("成功关闭数据重置标志...");
            	}
            } catch (Exception e) { //NOSONAR
                log.error("load init data failed.", e);
            }
        }
    }

	private void closeConfigCreateMode() throws URISyntaxException, IOException{
    	File configFile = new File(DbUnitUtils.class.getResource(APP_CONFIG_FILE_NAME).toURI());
    	FileInputStream finput = new FileInputStream(configFile);
    	BufferedReader reader = new BufferedReader(new InputStreamReader(finput));
        List<String> strList = new ArrayList<String>();
    	try{
	        String line = null;
	        while((line = reader.readLine()) != null){
	        	if(line.trim().startsWith(INIT_MODE_PARAM_NAME)){
	        		line = line.replaceAll(CREATE_ONCE, "none");
	        	}
	        	strList.add(line);
	        }
    	}finally{
	    	reader.close();
	    }
    	finput.close();
    	FileOutputStream foutput = new FileOutputStream(configFile);
    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(foutput));
    	try{
	    	for(String writeLine: strList){
	    		writer.write(writeLine);
	    		writer.newLine();
	    	}
    	}finally{
    		writer.close();
    	}
    	foutput.close();
    }

    private void registerConverter(){
//		ConvertUtils.register(new Converter2Number(), Double.class);
//		ConvertUtils.register(new Converter2Number(), Integer.class);
//		ConvertUtils.register(new Converter2Date(), java.sql.Date.class);
//		ConvertUtils.register(new Converter2Date(), java.util.Date.class);
//		ConvertUtils.register(new Converter2String(), String.class);
    }

    private void loadOnceData(){
    	if(StringUtils.isNotBlank(loadAndDeleteDataPath)){
            try {
            	URL url = DbUnitUtils.class.getResource(loadAndDeleteDataPath);
            	if(url != null){
	            	File deleteFile = new File(url.toURI());
	            	if(deleteFile.exists()){
	                	log.info("发现数据补丁，装入并删除补丁数据 " + loadAndDeleteDataPath + "...");
						DbUnitUtils.appendDbUnitData(dataSource, schema, dbType, replacementMap, new String[]{loadAndDeleteDataPath});
		            	if(deleteFile.delete()){
		                    log.info("success load and delete customer data file: " + deleteFile.getPath());
		            	}
		            	log.info("装入并自动删除成功...");
	            	}
            	}else{
                    log.warn("补丁数据 " + loadAndDeleteDataPath + " 未发现，跳过.");
            	}
			} catch (Exception e) { //NOSONAR
                log.error("加载补丁数据失败.", e);
			}
    	}
    }

	private void buildIndex(){
    	log.info("开始重建索引...");
		Map<String, IHasSearchBuilder> searchBuilderMap = SpringContextHelper.getBeansOfType(IHasSearchBuilder.class);
		List<IHasSearchBuilder> searchBuilderList = new ArrayList<IHasSearchBuilder>(searchBuilderMap.values());
		searchBuilderList.sort(new Comparator<IHasSearchBuilder>() {
			public int compare(IHasSearchBuilder lhs, IHasSearchBuilder rhs) {
				return lhs.getPriority().compareTo(rhs.getPriority()); //当只设置少数几个Async线程干活时，会按照顺序来建立
			}
		});
//		searchBuilderList.sort((lhs,rhs) -> lhs.getPriority().compareTo(rhs.getPriority())); //jetty8跟lambda表达式冲突
		for(IHasSearchBuilder searchBuilder: searchBuilderList){
			searchBuilder.buildAllIndex(); //异步建立索引
		}
	}
}
