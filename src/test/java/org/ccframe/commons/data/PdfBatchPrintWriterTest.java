package org.ccframe.commons.data;
import java.util.ArrayList;
import java.util.List;

import org.ccframe.commons.data.PdfBatchPrintWriter.Zoom;
import org.ccframe.commons.data.PdfCellPrintData.BorderMode;
import org.ccframe.commons.data.PdfCellPrintData.VerticalAlign;
import org.ccframe.commons.data.PdfPrintData.HorizontalAlign;
import org.ccframe.commons.util.WebContextHolder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.PageSize;

//@Ignore("本地测试")
public class PdfBatchPrintWriterTest {

	private static final String WAR_PATH = "war";
	
	@BeforeClass
	public static void beforeClass(){
		WebContextHolder.setWarPath(WAR_PATH); //实际的WAR目录
	}
	
	/**
	 * 浮动定位套打测试
	 */
	@Test
	public void testPrintArea(){
		List<PdfAreaPrintData> printList = new ArrayList<>();
		printList.add(new PdfAreaPrintData(10, 10, 200, 30, 12, "大家好fdsfasfd33.2423你好我好大家好这个🌾是emoji🐠✿在❤里√没有➹犇🐹"));
		printList.add(new PdfAreaPrintData(120, 150, 180, 60, 14, "大家好fdsfasfd33.2423你好我好大家好这个是emoji🐠✿在❤里√没有➹犇🐹"));
		new PdfBatchPrintWriter(PageSize.A5.getWidth(), PageSize.A5.getHeight(), WAR_PATH + "\\testPrintArea1.pdf", null, Zoom.ZOOM_96_TO_72_DPI)
			.floatLayerPrint(printList)
			.endPage();
	}

	/**
	 * 表格定位打印测试 6x4 区域测试
	 */
	@Test
	public void testPrintCell(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 40, 6, null, 16, "这个是标题");
		titleData.setFontspace(50);
		printList.add(titleData);

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 4, "打印时间：2016-8-15"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 2, "打印人：系统管理员"));

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "用户ID"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "姓名"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "证件号码"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "缴费总数"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "缴费时间"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "备注🆎"));
		
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "43F423"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "张三"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "AFSDAFER"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "12343.3"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "2005-10-3"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "VFDSFSD"));

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "EE3"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "李四"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "ERWE332432432"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "34.2"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "2005-10-3"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "➹Hello World✿"));

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "EWE"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "王五"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "fdsaeee33"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "13323"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "2005-10-3"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "❄冬天来了⛄"));

		
		new PdfBatchPrintWriter(PageSize.A5.getHeight(), PageSize.A5.getWidth(), WAR_PATH + "\\testPrintCell1.pdf", null, Zoom.DEFAULT)
			.gridLayerPrint(printList, 40f, 45f, 90f, 45f, 45f, 83f)
			.endPage();
	}

	@Test
	public void testPrint2Layer(){
		List<PdfAreaPrintData> printList1 = new ArrayList<>();
		printList1.add(new PdfAreaPrintData(10, 10, 200, 30, 12, "大家好fdsfasfd33.2423你好我好大家好这个🌾是emoji🐠✿在❤里√没有➹犇🐹"));
		printList1.add(new PdfAreaPrintData(120, 150, 180, 60, 14, "大家好fdsfasfd33.2423你好我好大家好这个是emoji🐠✿在❤里√没有➹犇🐹"));
		
		List<PdfCellPrintData> printList2 = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 40, 6, null, 16, "这个是标题");
		titleData.setFontspace(50);
		printList2.add(titleData);

		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 4, "打印时间：2016-8-15"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 2, "打印人：系统管理员"));

		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "用户ID"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "姓名"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "证件号码"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "缴费总数"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "缴费时间"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "备注🆎"));
		
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "43F423"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "张三"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "AFSDAFER"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "12343.3"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "2005-10-3"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "VFDSFSD"));

		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "EE3"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "李四"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "ERWE332432432"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "34.2"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "2005-10-3"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_DASH, null, "➹Hello World✿"));

		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "EWE"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "王五"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "fdsaeee33"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "13323"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "2005-10-3"));
		printList2.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "❄冬天来了⛄"));

		
		new PdfBatchPrintWriter(PageSize.A5.getHeight(), PageSize.A5.getWidth(), WAR_PATH + "\\testPrint2Layer3.pdf", null, Zoom.DEFAULT)
			.floatLayerPrint(printList1)
			.gridLayerPrint(printList2, 40f, 45f, 90f, 45f, 45f, 83f)
			.endPage();
	}
	
	@Test
	//处方筏
	public void printPrescriptionGoodsItem(){

		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 80, 11, null, 30, "广东技术师范学院门诊部\n处方筏");
		titleData.setFontspace(50);
		printList.add(titleData);

		PdfCellPrintData szgyTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 30, 11, null, 14, "省直公医");
		szgyTip.setFontspace(10);
		printList.add(szgyTip);
		
		PdfCellPrintData idTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 28, 8, null, 10, "医保卡号/校医疗证号: 440711199006279093");
		idTip.setFontspace(1);
		printList.add(idTip);
		PdfCellPrintData prescriptionIdTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 28, 3, null, 10, "诊断流水号: 201691905");
		prescriptionIdTip.setFontspace(1);
		printList.add(prescriptionIdTip);
		
		PdfCellPrintData nameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 司马张三张三");
		nameTip.setFontspace(1);
		printList.add(nameTip);
		PdfCellPrintData sexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		sexTip.setFontspace(1);
		printList.add(sexTip);
		PdfCellPrintData ageTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 133");
		ageTip.setFontspace(1);
		printList.add(ageTip);
		PdfCellPrintData costTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "人员费别: 学生学生100%");
		costTip.setFontspace(1);
		printList.add(costTip);
		
		PdfCellPrintData classTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 8, null, 10, "工作部门/所在班级: 14服装（1）班");
		classTip.setFontspace(1);
		printList.add(classTip);
		PdfCellPrintData telTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18675622501");
		telTip.setFontspace(1);
		printList.add(telTip);

		PdfCellPrintData addressTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "住址: 白云校区 四栋 6楼 90");
		addressTip.setFontspace(1);
		printList.add(addressTip);
		PdfCellPrintData cumulativeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "就诊次数累计: 333");
		cumulativeTip.setFontspace(1);
		printList.add(cumulativeTip);
		PdfCellPrintData cumulativeCostTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "近12个消费: ￥444.9");
		cumulativeCostTip.setFontspace(1);
		printList.add(cumulativeCostTip);

		PdfCellPrintData resultTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 6, null, 10, "诊断结果: 流行性感冒");
		resultTip.setFontspace(1);
		printList.add(resultTip);
		PdfCellPrintData issueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "开具时间: 2516年09月19日");
		issueDateTip.setFontspace(1);
		printList.add(issueDateTip);

		PdfCellPrintData rpTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 40, 11, null, 25, "Rp.");
		rpTip.setFontspace(50);
		printList.add(rpTip);
		
		PdfCellPrintData rpTemp = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 210, 1, null, 10, "");
		rpTemp.setFontspace(50);
		printList.add(rpTemp);
		PdfCellPrintData rpTextData = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 210, 10, null, 10, "1.百灵鸟维C银翘片    24片/盒  *  2盒 \n     用法：口服  一次2片  一日3次 \n2.阿拉明注射液     1ml:9mg*9支  *  1支\n     用法：肌注  5mg 阿拉明注射液 + 生理盐水 500ml / 次   一日2次\n3.【物品名称】     【规格】  *  【数量】【剂量单位】\n    用法（口服）：【服药用法】  【服药剂量】【服药周期】\n    用法（注射）：【注射类型】  【注射补充用法】");
		rpTextData.setFontspace(50);
		printList.add(rpTextData);

		PdfCellPrintData doctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 5, null, 10, "接诊医生: Admin【打印】");
		doctorTip.setFontspace(10);
		printList.add(doctorTip);
		PdfCellPrintData signatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "医师（签章）:");
		signatureTip.setFontspace(10);
		printList.add(signatureTip);
		PdfCellPrintData priceTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "药品金额: ￥90");
		priceTip.setFontspace(10);
		printList.add(priceTip);

		PdfCellPrintData chargerTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 5, null, 10, "收费员（签章）:");
		chargerTip.setFontspace(10);
		printList.add(chargerTip);
		PdfCellPrintData examineDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "审核药师:");
		examineDoctorTip.setFontspace(10);
		printList.add(examineDoctorTip);
		PdfCellPrintData checkDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "核对、发药药师:");
		checkDoctorTip.setFontspace(10);
		printList.add(checkDoctorTip);
		
		
		PdfCellPrintData diagnosisTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n转诊单");
		diagnosisTitleData.setFontspace(50);
		printList.add(diagnosisTitleData);
		
		PdfCellPrintData diagnosisIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		diagnosisIdTip.setFontspace(10);
		printList.add(diagnosisIdTip);
		PdfCellPrintData diagnosisTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 10345678");
		diagnosisTreatmentTip.setFontspace(10);
		printList.add(diagnosisTreatmentTip);
		
		PdfCellPrintData diagnosisNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名:张全蛋张全蛋");
		diagnosisNameTip.setFontspace(10);
		printList.add(diagnosisNameTip);
		PdfCellPrintData diagnosisSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		diagnosisSexTip.setFontspace(10);
		printList.add(diagnosisSexTip);
		PdfCellPrintData diagnosisAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 122");
		diagnosisAgeTip.setFontspace(10);
		printList.add(diagnosisAgeTip);
		PdfCellPrintData diagnosisTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		diagnosisTelTip.setFontspace(10);
		printList.add(diagnosisTelTip);
		
		PdfCellPrintData diagnosisHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 6, null, 10, "既往病史: 高血压");
		diagnosisHistoryTip.setFontspace(10);
		printList.add(diagnosisHistoryTip);
		PdfCellPrintData diagnosisIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "身份证号: 412824188273649376");
		diagnosisIdCardTip.setFontspace(10);
		printList.add(diagnosisIdCardTip);
		
		PdfCellPrintData diagnosisIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "病情诊断");
		diagnosisIllnessTip.setFontspace(10);
		printList.add(diagnosisIllnessTip);
		PdfCellPrintData diagnosisIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐");
		diagnosisIllnessText.setFontspace(10);
		printList.add(diagnosisIllnessText);
		
		PdfCellPrintData diagnosisReferralTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断信息");
		diagnosisReferralTip.setFontspace(10);
		printList.add(diagnosisReferralTip);
		
		PdfCellPrintData diagnosisHospitalTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 8, null, 10, "转诊医院: 南方医科大学第三附属医院");
		diagnosisHospitalTip.setFontspace(10);
		printList.add(diagnosisHospitalTip);
		PdfCellPrintData diagnosisDepartmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "转诊科室: 内科");
		diagnosisDepartmentTip.setFontspace(10);
		printList.add(diagnosisDepartmentTip);
		
		PdfCellPrintData dianosisDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "转诊期限: 2016年9月2号-2016年9月10号");
		dianosisDateTip.setFontspace(10);
		printList.add(dianosisDateTip);
		
		PdfCellPrintData diagnosisDoctorNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_DASH, 25, 5, null, 10, "医师(签章): admin");
		diagnosisDoctorNameTip.setFontspace(10);
		printList.add(diagnosisDoctorNameTip);	
		PdfCellPrintData diagnosisIssueDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_DASH, 25, 5, null, 10, "开具日期: 2016年12月22号");
		diagnosisIssueDateTip.setFontspace(10);
		printList.add(diagnosisIssueDateTip);
		PdfCellPrintData diagnosisEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_DASH, 25, 1, null, 10, "(盖章生效)");
		diagnosisEffectTip.setFontspace(10);
		printList.add(diagnosisEffectTip);
		
		PdfCellPrintData stubDiagnosisTitle = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n转诊存根");
		stubDiagnosisTitle.setFontspace(50);
		printList.add(stubDiagnosisTitle);
		
		PdfCellPrintData stubDiagnosisIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		stubDiagnosisIdTip.setFontspace(10);
		printList.add(stubDiagnosisIdTip);
		PdfCellPrintData stubDiagnosisTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 10345678");
		stubDiagnosisTreatmentTip.setFontspace(10);
		printList.add(stubDiagnosisTreatmentTip);
		
		PdfCellPrintData stubDiagnosisNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 张全蛋张全蛋");
		stubDiagnosisNameTip.setFontspace(10);
		printList.add(stubDiagnosisNameTip);
		PdfCellPrintData stubDiagnosisSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		stubDiagnosisSexTip.setFontspace(10);
		printList.add(stubDiagnosisSexTip);
		PdfCellPrintData stubDiagnosisAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 99");
		stubDiagnosisAgeTip.setFontspace(10);
		printList.add(stubDiagnosisAgeTip);
		PdfCellPrintData stubDiagnosisTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		stubDiagnosisTelTip.setFontspace(10);
		printList.add(stubDiagnosisTelTip);

		
		PdfCellPrintData stubDiagnosisHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 6, null, 10, "既往病史: 高血压");
		stubDiagnosisHistoryTip.setFontspace(10);
		printList.add(stubDiagnosisHistoryTip);
		PdfCellPrintData stubDiagnosisIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "身份证号: 410824188273649376");
		stubDiagnosisIdCardTip.setFontspace(10);
		printList.add(stubDiagnosisIdCardTip);
		
		PdfCellPrintData stubDiagnosisIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "病情诊断");
		stubDiagnosisIllnessTip.setFontspace(10);
		printList.add(stubDiagnosisIllnessTip);
		PdfCellPrintData stubDiagnosisIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐");
		stubDiagnosisIllnessText.setFontspace(10);
		printList.add(stubDiagnosisIllnessText);
		
		PdfCellPrintData stubDiagnosisReferralTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断信息");
		stubDiagnosisReferralTip.setFontspace(10);
		printList.add(stubDiagnosisReferralTip);
		
		PdfCellPrintData stubDiagnosisHospitalTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 7, null, 10, "转诊医院: 南方医科大学第三附属医院");
		stubDiagnosisHospitalTip.setFontspace(10);
		printList.add(stubDiagnosisHospitalTip);
		PdfCellPrintData stubDiagnosisDepartmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "转诊科室: 内科");
		stubDiagnosisDepartmentTip.setFontspace(10);
		printList.add(stubDiagnosisDepartmentTip);
		
		PdfCellPrintData stubDiagnosisDianosisDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "转诊期限: 2016年9月2号-2016年9月10号");
		stubDiagnosisDianosisDateTip.setFontspace(10);
		printList.add(stubDiagnosisDianosisDateTip);
		
		PdfCellPrintData stubDiagnosisIssueDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 6, null, 10, "开具日期: 2016年12月29号");
		stubDiagnosisIssueDateTip.setFontspace(10);
		printList.add(stubDiagnosisIssueDateTip);
		PdfCellPrintData stubDiagnosisEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "(盖章生效)");
		stubDiagnosisEffectTip.setFontspace(10);
		printList.add(stubDiagnosisEffectTip);

		
		PdfCellPrintData leaveTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n病假单");
		leaveTitleData.setFontspace(50);
		printList.add(leaveTitleData);
		
		PdfCellPrintData leaveIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		leaveIdTip.setFontspace(10);
		printList.add(leaveIdTip);
		PdfCellPrintData leaveTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		leaveTreatmentTip.setFontspace(10);
		printList.add(leaveTreatmentTip);
		
		PdfCellPrintData leaveNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名:张全蛋张全蛋");
		leaveNameTip.setFontspace(10);
		printList.add(leaveNameTip);
		PdfCellPrintData leaveSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		leaveSexTip.setFontspace(10);
		printList.add(leaveSexTip);
		PdfCellPrintData leaveAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 122");
		leaveAgeTip.setFontspace(10);
		printList.add(leaveAgeTip);
		PdfCellPrintData leaveTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		leaveTelTip.setFontspace(10);
		printList.add(leaveTelTip);
		
		PdfCellPrintData leaveHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "工作部门/所在班级: 计科学院14级服装设计1班");
		leaveHistoryTip.setFontspace(10);
		printList.add(leaveHistoryTip);
		PdfCellPrintData leaveIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "住址: 校本部 四栋5楼110");
		leaveIdCardTip.setFontspace(10);
		printList.add(leaveIdCardTip);
		
		PdfCellPrintData leaveIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断结果");
		leaveIllnessTip.setFontspace(10);
		printList.add(leaveIllnessTip);
		PdfCellPrintData leaveIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐，流行性感冒");
		leaveIllnessText.setFontspace(10);
		printList.add(leaveIllnessText);
		
		PdfCellPrintData leaveTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "休假信息");
		leaveTip.setFontspace(10);
		printList.add(leaveTip);
		
		PdfCellPrintData leaveDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "休假日期: 2016年09月19日-2016年09月23日");
		leaveDateTip.setFontspace(10);
		printList.add(leaveDateTip);
		PdfCellPrintData leaveDaysTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "休假天数: 3天");
		leaveDaysTip.setFontspace(10);
		printList.add(leaveDaysTip);
		
		PdfCellPrintData leaveDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "接诊医生: Admin【打印】");
		leaveDoctorTip.setFontspace(10);
		printList.add(leaveDoctorTip);
		PdfCellPrintData leaveSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "医师（签章）:");
		leaveSignatureTip.setFontspace(10);
		printList.add(leaveSignatureTip);
		PdfCellPrintData leaveIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "开具日期: 2016年12月29号");
		leaveIssueDateTip.setFontspace(10);
		printList.add(leaveIssueDateTip);
	
		PdfCellPrintData leaveEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_DASH, 25, 11, null, 10, "(盖章生效)");
		leaveEffectTip.setFontspace(10);
		printList.add(leaveEffectTip);
		
		PdfCellPrintData stubLeaveTitle = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n病假存根");
		stubLeaveTitle.setFontspace(50);
		printList.add(stubLeaveTitle);
		
		PdfCellPrintData stubLeaveIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		stubLeaveIdTip.setFontspace(10);
		printList.add(stubLeaveIdTip);
		PdfCellPrintData stubLeaveTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		stubLeaveTreatmentTip.setFontspace(10);
		printList.add(stubLeaveTreatmentTip);
		
		PdfCellPrintData stubLeaveNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名:张全蛋张全蛋");
		stubLeaveNameTip.setFontspace(10);
		printList.add(stubLeaveNameTip);
		PdfCellPrintData stubLeaveSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		stubLeaveSexTip.setFontspace(10);
		printList.add(stubLeaveSexTip);
		PdfCellPrintData stubLeaveAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 122");
		stubLeaveAgeTip.setFontspace(10);
		printList.add(stubLeaveAgeTip);
		PdfCellPrintData stubLeaveTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		stubLeaveTelTip.setFontspace(10);
		printList.add(stubLeaveTelTip);
		
		PdfCellPrintData stubLeaveHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "工作部门/所在班级: 计科学院14级服装设计1班");
		stubLeaveHistoryTip.setFontspace(10);
		printList.add(stubLeaveHistoryTip);
		PdfCellPrintData stubLeaveIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "住址: 校本部 四栋5楼110");
		stubLeaveIdCardTip.setFontspace(10);
		printList.add(stubLeaveIdCardTip);
		
		PdfCellPrintData stubLeaveIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断结果");
		stubLeaveIllnessTip.setFontspace(10);
		printList.add(stubLeaveIllnessTip);
		
		PdfCellPrintData stubLeaveIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐，流行性感冒");
		stubLeaveIllnessText.setFontspace(10);
		printList.add(stubLeaveIllnessText);
		
		PdfCellPrintData stubLeaveLeaveTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "休假信息");
		stubLeaveLeaveTip.setFontspace(10);
		printList.add(stubLeaveLeaveTip);
		
		PdfCellPrintData stubLeaveLeaveDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "休假日期: 2016年09月19日-2016年09月23日");
		stubLeaveLeaveDateTip.setFontspace(10);
		printList.add(stubLeaveLeaveDateTip);
		PdfCellPrintData stubLeaveLeaveDaysTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "休假天数: 3天");
		stubLeaveLeaveDaysTip.setFontspace(10);
		printList.add(stubLeaveLeaveDaysTip);
		
		PdfCellPrintData stubLeaveDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "接诊医生: Admin【打印】");
		stubLeaveDoctorTip.setFontspace(10);
		printList.add(stubLeaveDoctorTip);
		PdfCellPrintData stubLeaveSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "医师（签章）:");
		stubLeaveSignatureTip.setFontspace(10);
		printList.add(stubLeaveSignatureTip);
		PdfCellPrintData stubLeaveIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "开具日期: 2016年12月29号");
		stubLeaveIssueDateTip.setFontspace(10);
		printList.add(stubLeaveIssueDateTip);
	
		PdfCellPrintData stubLeaveEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.NONE, 25, 11, null, 10, "(盖章生效)");
		stubLeaveEffectTip.setFontspace(10);
		printList.add(stubLeaveEffectTip);
		
		
		PdfCellPrintData injectionTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, BorderMode.NONE, 80, 11, null, 30, "广东技术师范学院门诊部\n注射单");
		injectionTitleData.setFontspace(50);
		printList.add(injectionTitleData);
		
		PdfCellPrintData injectionIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		injectionIdTip.setFontspace(10);
		printList.add(injectionIdTip);
		PdfCellPrintData injectionTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		injectionTreatmentTip.setFontspace(10);
		printList.add(injectionTreatmentTip);
		
		PdfCellPrintData injectionNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 司马张全张全");
		injectionNameTip.setFontspace(10);
		printList.add(injectionNameTip);
		PdfCellPrintData injectionSexTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		injectionSexTip.setFontspace(10);
		printList.add(injectionSexTip);
		PdfCellPrintData injectionAgeTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 123");
		injectionAgeTip.setFontspace(10);
		printList.add(injectionAgeTip);
		PdfCellPrintData injectionCostTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "人员费别: 学生学生10%");
		injectionCostTip.setFontspace(10);
		printList.add(injectionCostTip);
		
		PdfCellPrintData injectionFreePayTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "医保: 有");
		injectionFreePayTip.setFontspace(10);
		printList.add(injectionFreePayTip);
		PdfCellPrintData injectionHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "工作部门/所在班级: 14级服装设计1班");
		injectionHistoryTip.setFontspace(10);
		printList.add(injectionHistoryTip);
		PdfCellPrintData injectionIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "开具日期: 2016年12月29号");
		injectionIssueDateTip.setFontspace(10);
		printList.add(injectionIssueDateTip);
		
		PdfCellPrintData injectionTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "注射用法");
		injectionTip.setFontspace(10);
		printList.add(injectionTip);
		PdfCellPrintData injectionText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 260, 11, null, 10, "5mg 阿拉明注射液 + 生理盐水 500ml/次 一日两次");
		injectionText.setFontspace(10);
		printList.add(injectionText);
		
		PdfCellPrintData injectionNoteCardTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "注射记录卡");
		injectionNoteCardTip.setFontspace(10);
		printList.add(injectionNoteCardTip);
		
		PdfCellPrintData injectionDateTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "处理日期");
		injectionDateTip.setFontspace(10);
		printList.add(injectionDateTip);
		PdfCellPrintData injectionDateText1 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionDateText1.setFontspace(10);
		printList.add(injectionDateText1);
		PdfCellPrintData injectionDateText2 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionDateText2.setFontspace(10);
		printList.add(injectionDateText2);		
		PdfCellPrintData injectionDateText3 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionDateText3.setFontspace(10);
		printList.add(injectionDateText3);		
		PdfCellPrintData injectionDateText4 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionDateText4.setFontspace(10);
		printList.add(injectionDateText4);
		PdfCellPrintData injectionDateText5 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionDateText5.setFontspace(10);
		printList.add(injectionDateText5);
		
		PdfCellPrintData injectionImplementTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "执行护士");
		injectionImplementTip.setFontspace(10);
		printList.add(injectionImplementTip);
		PdfCellPrintData injectionImplementText1 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionImplementText1.setFontspace(10);
		printList.add(injectionImplementText1);
		PdfCellPrintData injectionImplementText2 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionImplementText2.setFontspace(10);
		printList.add(injectionImplementText2);
		PdfCellPrintData injectionImplementText3 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionImplementText3.setFontspace(10);
		printList.add(injectionImplementText3);
		PdfCellPrintData injectionImplementText4 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionImplementText4.setFontspace(10);
		printList.add(injectionImplementText4);
		PdfCellPrintData injectionImplementText5 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionImplementText5.setFontspace(10);
		printList.add(injectionImplementText5);

		PdfCellPrintData injectionDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 5, null, 10, "接诊医生: Admin");
		injectionDoctorTip.setFontspace(10);
		printList.add(injectionDoctorTip);
		PdfCellPrintData injectionSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "医师（签章）:");
		injectionSignatureTip.setFontspace(10);
		printList.add(injectionSignatureTip);
		PdfCellPrintData injectionPriceTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "治疗费用: ￥32210.0");
		injectionPriceTip.setFontspace(10);
		printList.add(injectionPriceTip);
		
		
		PdfCellPrintData exchangeTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 80, 11, null, 30, "广东技术师范学院门诊部\n换药单");
		exchangeTitleData.setFontspace(50);
		printList.add(exchangeTitleData);
		
		PdfCellPrintData exchangeIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		exchangeIdTip.setFontspace(10);
		printList.add(exchangeIdTip);
		PdfCellPrintData exchangeTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		exchangeTreatmentTip.setFontspace(10);
		printList.add(exchangeTreatmentTip);	
		
		PdfCellPrintData exchangeNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 司马张全张全");
		exchangeNameTip.setFontspace(10);
		printList.add(exchangeNameTip);
		PdfCellPrintData exchangeSexTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		exchangeSexTip.setFontspace(10);
		printList.add(exchangeSexTip);
		PdfCellPrintData exchangeAgeTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 133");
		exchangeAgeTip.setFontspace(10);
		printList.add(exchangeAgeTip);
		PdfCellPrintData exchangeCostTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "人员费别: 学生学生10%");
		exchangeCostTip.setFontspace(10);
		printList.add(exchangeCostTip);
		
		PdfCellPrintData exchangeFreePayTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "医保: 有");
		exchangeFreePayTip.setFontspace(10);
		printList.add(exchangeFreePayTip);
		PdfCellPrintData exchangeHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "工作部门/所在班级: 14级服装设计1班");
		exchangeHistoryTip.setFontspace(10);
		printList.add(exchangeHistoryTip);
		PdfCellPrintData exchangeIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "开具日期: 2016年12月29号");
		exchangeIssueDateTip.setFontspace(10);
		printList.add(exchangeIssueDateTip);
		
		PdfCellPrintData exchangeWayTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "换药用法");
		exchangeWayTip.setFontspace(10);
		printList.add(exchangeWayTip);
		PdfCellPrintData exchangeWayText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 250, 11, null, 10, "换药部位:手臂 \n换药范围大小:创面、伤口冲洗\n换药次数:一日两次\n换药方法:0.1-0.2%纱布");
		exchangeWayText.setFontspace(10);
		printList.add(exchangeWayText);
		PdfCellPrintData exchangeNoteCardTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "换药记录卡");
		exchangeNoteCardTip.setFontspace(10);
		printList.add(exchangeNoteCardTip);
		
		PdfCellPrintData exchangeDateTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "处理日期");
		exchangeDateTip.setFontspace(10);
		printList.add(exchangeDateTip);
		PdfCellPrintData exchangeDateText1 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeDateText1.setFontspace(10);
		printList.add(exchangeDateText1);
		PdfCellPrintData exchangeDateText2 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeDateText2.setFontspace(10);
		printList.add(exchangeDateText2);		
		PdfCellPrintData exchangeDateText3 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeDateText3.setFontspace(10);
		printList.add(exchangeDateText3);		
		PdfCellPrintData exchangeDateText4 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeDateText4.setFontspace(10);
		printList.add(exchangeDateText4);
		PdfCellPrintData exchangeDateText5 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeDateText5.setFontspace(10);
		printList.add(exchangeDateText5);
		
		PdfCellPrintData exchangeImplementTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "执行护士");
		exchangeImplementTip.setFontspace(10);
		printList.add(exchangeImplementTip);
		PdfCellPrintData exchangeImplementText1 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeImplementText1.setFontspace(10);
		printList.add(exchangeImplementText1);
		PdfCellPrintData exchangeImplementText2 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeImplementText2.setFontspace(10);
		printList.add(exchangeImplementText2);
		PdfCellPrintData exchangeImplementText3 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeImplementText3.setFontspace(10);
		printList.add(exchangeImplementText3);
		PdfCellPrintData exchangeImplementText4 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeImplementText4.setFontspace(10);
		printList.add(exchangeImplementText4);
		PdfCellPrintData exchangeImplementText5 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeImplementText5.setFontspace(10);
		printList.add(exchangeImplementText5);
		
		PdfCellPrintData exchangeDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 5, null, 10, "接诊医生: Admin");
		exchangeDoctorTip.setFontspace(10);
		printList.add(exchangeDoctorTip);
		PdfCellPrintData exchangeSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "医师（签章）:");
		exchangeSignatureTip.setFontspace(10);
		printList.add(exchangeSignatureTip);
		PdfCellPrintData exchangePriceTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "治疗费用: ￥2310.0");
		exchangePriceTip.setFontspace(10);
		printList.add(exchangePriceTip);
		
		new PdfBatchPrintWriter(PageSize.A5.getWidth(), PageSize.A5.getHeight(), WAR_PATH + "\\处方筏.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 35f, 10f, 25f, 68f, 44f, 50f, 44f, 35f, 70f, 13f, 90f)
		.endPage();
	}
	
	@Test
	//转诊单  52 97
	public void printDianosis(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData diagnosisTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, BorderMode.NONE, 97, 11, null, 18, "广东技术师范学院门诊部\n转诊单");
		diagnosisTitleData.setFontspace(50);
		printList.add(diagnosisTitleData);
		
		PdfCellPrintData diagnosisIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		diagnosisIdTip.setFontspace(10);
		printList.add(diagnosisIdTip);
		PdfCellPrintData diagnosisTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 10345678");
		diagnosisTreatmentTip.setFontspace(10);
		printList.add(diagnosisTreatmentTip);
		
		PdfCellPrintData diagnosisNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名:张全蛋张全蛋");
		diagnosisNameTip.setFontspace(10);
		printList.add(diagnosisNameTip);
		PdfCellPrintData diagnosisSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		diagnosisSexTip.setFontspace(10);
		printList.add(diagnosisSexTip);
		PdfCellPrintData diagnosisAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 122");
		diagnosisAgeTip.setFontspace(10);
		printList.add(diagnosisAgeTip);
		PdfCellPrintData diagnosisTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		diagnosisTelTip.setFontspace(10);
		printList.add(diagnosisTelTip);
		
		PdfCellPrintData diagnosisHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 6, null, 10, "既往病史: 高血压");
		diagnosisHistoryTip.setFontspace(10);
		printList.add(diagnosisHistoryTip);
		PdfCellPrintData diagnosisIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "身份证号: 412824188273649376");
		diagnosisIdCardTip.setFontspace(10);
		printList.add(diagnosisIdCardTip);
		
		PdfCellPrintData diagnosisIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "病情诊断");
		diagnosisIllnessTip.setFontspace(10);
		printList.add(diagnosisIllnessTip);
		PdfCellPrintData diagnosisIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐");
		diagnosisIllnessText.setFontspace(10);
		printList.add(diagnosisIllnessText);
		
		PdfCellPrintData diagnosisReferralTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断信息");
		diagnosisReferralTip.setFontspace(10);
		printList.add(diagnosisReferralTip);
		
		PdfCellPrintData diagnosisHospitalTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 8, null, 10, "转诊医院: 南方医科大学第三附属医院");
		diagnosisHospitalTip.setFontspace(10);
		printList.add(diagnosisHospitalTip);
		PdfCellPrintData diagnosisDepartmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "转诊科室: 内科");
		diagnosisDepartmentTip.setFontspace(10);
		printList.add(diagnosisDepartmentTip);
		
		PdfCellPrintData dianosisDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "转诊期限: 2016年9月2号-2016年9月10号");
		dianosisDateTip.setFontspace(10);
		printList.add(dianosisDateTip);
		
		PdfCellPrintData diagnosisDoctorNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_DASH, 25, 5, null, 10, "医师(签章): admin");
		diagnosisDoctorNameTip.setFontspace(10);
		printList.add(diagnosisDoctorNameTip);	
		PdfCellPrintData diagnosisIssueDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_DASH, 25, 5, null, 10, "开具日期: 2016年12月22号");
		diagnosisIssueDateTip.setFontspace(10);
		printList.add(diagnosisIssueDateTip);
		PdfCellPrintData diagnosisEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_DASH, 25, 1, null, 10, "(盖章生效)");
		diagnosisEffectTip.setFontspace(10);
		printList.add(diagnosisEffectTip);
		
		PdfCellPrintData stubDiagnosisTitle = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n转诊存根");
		stubDiagnosisTitle.setFontspace(50);
		printList.add(stubDiagnosisTitle);
		
		PdfCellPrintData stubDiagnosisIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		stubDiagnosisIdTip.setFontspace(10);
		printList.add(stubDiagnosisIdTip);
		PdfCellPrintData stubDiagnosisTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 10345678");
		stubDiagnosisTreatmentTip.setFontspace(10);
		printList.add(stubDiagnosisTreatmentTip);
		
		PdfCellPrintData stubDiagnosisNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 张全蛋张全蛋");
		stubDiagnosisNameTip.setFontspace(10);
		printList.add(stubDiagnosisNameTip);
		PdfCellPrintData stubDiagnosisSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		stubDiagnosisSexTip.setFontspace(10);
		printList.add(stubDiagnosisSexTip);
		PdfCellPrintData stubDiagnosisAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 99");
		stubDiagnosisAgeTip.setFontspace(10);
		printList.add(stubDiagnosisAgeTip);
		PdfCellPrintData stubDiagnosisTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		stubDiagnosisTelTip.setFontspace(10);
		printList.add(stubDiagnosisTelTip);

		
		PdfCellPrintData stubDiagnosisHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 6, null, 10, "既往病史: 高血压");
		stubDiagnosisHistoryTip.setFontspace(10);
		printList.add(stubDiagnosisHistoryTip);
		PdfCellPrintData stubDiagnosisIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "身份证号: 410824188273649376");
		stubDiagnosisIdCardTip.setFontspace(10);
		printList.add(stubDiagnosisIdCardTip);
		
		PdfCellPrintData stubDiagnosisIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "病情诊断");
		stubDiagnosisIllnessTip.setFontspace(10);
		printList.add(stubDiagnosisIllnessTip);
		PdfCellPrintData stubDiagnosisIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐");
		stubDiagnosisIllnessText.setFontspace(10);
		printList.add(stubDiagnosisIllnessText);
		
		PdfCellPrintData stubDiagnosisReferralTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断信息");
		stubDiagnosisReferralTip.setFontspace(10);
		printList.add(stubDiagnosisReferralTip);
		
		PdfCellPrintData stubDiagnosisHospitalTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 7, null, 10, "转诊医院: 南方医科大学第三附属医院");
		stubDiagnosisHospitalTip.setFontspace(10);
		printList.add(stubDiagnosisHospitalTip);
		PdfCellPrintData stubDiagnosisDepartmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "转诊科室: 内科");
		stubDiagnosisDepartmentTip.setFontspace(10);
		printList.add(stubDiagnosisDepartmentTip);
		
		PdfCellPrintData stubDiagnosisDianosisDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "转诊期限: 2016年9月2号-2016年9月10号");
		stubDiagnosisDianosisDateTip.setFontspace(10);
		printList.add(stubDiagnosisDianosisDateTip);
		
		PdfCellPrintData stubDiagnosisIssueDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 6, null, 10, "开具日期: 2016年12月29号");
		stubDiagnosisIssueDateTip.setFontspace(10);
		printList.add(stubDiagnosisIssueDateTip);
		PdfCellPrintData stubDiagnosisEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "(盖章生效)");
		stubDiagnosisEffectTip.setFontspace(10);
		printList.add(stubDiagnosisEffectTip);
		
		new PdfBatchPrintWriter(PageSize.A5.getWidth(), PageSize.A5.getHeight(), WAR_PATH + "\\转诊单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 35f, 10f, 25f, 68f, 44f, 50f, 44f, 35f, 70f, 13f, 90f)
		.endPage();
		
		
	}
	
	@Test
	//休假单
	public void printLeaveTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData leaveTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n病假单");
		leaveTitleData.setFontspace(50);
		printList.add(leaveTitleData);
		
		PdfCellPrintData leaveIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		leaveIdTip.setFontspace(10);
		printList.add(leaveIdTip);
		PdfCellPrintData leaveTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		leaveTreatmentTip.setFontspace(10);
		printList.add(leaveTreatmentTip);
		
		PdfCellPrintData leaveNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名:张全蛋张全蛋");
		leaveNameTip.setFontspace(10);
		printList.add(leaveNameTip);
		PdfCellPrintData leaveSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		leaveSexTip.setFontspace(10);
		printList.add(leaveSexTip);
		PdfCellPrintData leaveAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 122");
		leaveAgeTip.setFontspace(10);
		printList.add(leaveAgeTip);
		PdfCellPrintData leaveTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		leaveTelTip.setFontspace(10);
		printList.add(leaveTelTip);
		
		PdfCellPrintData leaveHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "工作部门/所在班级: 计科学院14级服装设计1班");
		leaveHistoryTip.setFontspace(10);
		printList.add(leaveHistoryTip);
		PdfCellPrintData leaveIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "住址: 校本部 四栋5楼110");
		leaveIdCardTip.setFontspace(10);
		printList.add(leaveIdCardTip);
		
		PdfCellPrintData leaveIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断结果");
		leaveIllnessTip.setFontspace(10);
		printList.add(leaveIllnessTip);
		PdfCellPrintData leaveIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐，流行性感冒");
		leaveIllnessText.setFontspace(10);
		printList.add(leaveIllnessText);
		
		PdfCellPrintData leaveTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "休假信息");
		leaveTip.setFontspace(10);
		printList.add(leaveTip);
		
		PdfCellPrintData leaveDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "休假日期: 2016年09月19日-2016年09月23日");
		leaveDateTip.setFontspace(10);
		printList.add(leaveDateTip);
		PdfCellPrintData leaveDaysTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "休假天数: 3天");
		leaveDaysTip.setFontspace(10);
		printList.add(leaveDaysTip);
		
		PdfCellPrintData leaveDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "接诊医生: Admin【打印】");
		leaveDoctorTip.setFontspace(10);
		printList.add(leaveDoctorTip);
		PdfCellPrintData leaveSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "医师（签章）:");
		leaveSignatureTip.setFontspace(10);
		printList.add(leaveSignatureTip);
		PdfCellPrintData leaveIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "开具日期: 2016年12月29号");
		leaveIssueDateTip.setFontspace(10);
		printList.add(leaveIssueDateTip);
	
		PdfCellPrintData leaveEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_DASH, 25, 11, null, 10, "(盖章生效)");
		leaveEffectTip.setFontspace(10);
		printList.add(leaveEffectTip);
		
		PdfCellPrintData stubLeaveTitle = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 52, 11, null, 18, "广东技术师范学院门诊部\n病假存根");
		stubLeaveTitle.setFontspace(50);
		printList.add(stubLeaveTitle);
		
		PdfCellPrintData stubLeaveIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		stubLeaveIdTip.setFontspace(10);
		printList.add(stubLeaveIdTip);
		PdfCellPrintData stubLeaveTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		stubLeaveTreatmentTip.setFontspace(10);
		printList.add(stubLeaveTreatmentTip);
		
		PdfCellPrintData stubLeaveNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名:张全蛋张全蛋");
		stubLeaveNameTip.setFontspace(10);
		printList.add(stubLeaveNameTip);
		PdfCellPrintData stubLeaveSexTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		stubLeaveSexTip.setFontspace(10);
		printList.add(stubLeaveSexTip);
		PdfCellPrintData stubLeaveAgeTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 122");
		stubLeaveAgeTip.setFontspace(10);
		printList.add(stubLeaveAgeTip);
		PdfCellPrintData stubLeaveTelTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "联系电话: 18819182253");
		stubLeaveTelTip.setFontspace(10);
		printList.add(stubLeaveTelTip);
		
		PdfCellPrintData stubLeaveHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "工作部门/所在班级: 计科学院14级服装设计1班");
		stubLeaveHistoryTip.setFontspace(10);
		printList.add(stubLeaveHistoryTip);
		PdfCellPrintData stubLeaveIdCardTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "住址: 校本部 四栋5楼110");
		stubLeaveIdCardTip.setFontspace(10);
		printList.add(stubLeaveIdCardTip);
		
		PdfCellPrintData stubLeaveIllnessTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "诊断结果");
		stubLeaveIllnessTip.setFontspace(10);
		printList.add(stubLeaveIllnessTip);
		
		PdfCellPrintData stubLeaveIllnessText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 11, null, 10, "发烧、呕吐，流行性感冒");
		stubLeaveIllnessText.setFontspace(10);
		printList.add(stubLeaveIllnessText);
		
		PdfCellPrintData stubLeaveLeaveTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "休假信息");
		stubLeaveLeaveTip.setFontspace(10);
		printList.add(stubLeaveLeaveTip);
		
		PdfCellPrintData stubLeaveLeaveDateTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 7, null, 10, "休假日期: 2016年09月19日-2016年09月23日");
		stubLeaveLeaveDateTip.setFontspace(10);
		printList.add(stubLeaveLeaveDateTip);
		PdfCellPrintData stubLeaveLeaveDaysTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 4, null, 10, "休假天数: 3天");
		stubLeaveLeaveDaysTip.setFontspace(10);
		printList.add(stubLeaveLeaveDaysTip);
		
		PdfCellPrintData stubLeaveDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 5, null, 10, "接诊医生: Admin【打印】");
		stubLeaveDoctorTip.setFontspace(10);
		printList.add(stubLeaveDoctorTip);
		PdfCellPrintData stubLeaveSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "医师（签章）:");
		stubLeaveSignatureTip.setFontspace(10);
		printList.add(stubLeaveSignatureTip);
		PdfCellPrintData stubLeaveIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "开具日期: 2016年12月29号");
		stubLeaveIssueDateTip.setFontspace(10);
		printList.add(stubLeaveIssueDateTip);
	
		PdfCellPrintData stubLeaveEffectTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.NONE, 25, 11, null, 10, "(盖章生效)");
		stubLeaveEffectTip.setFontspace(10);
		printList.add(stubLeaveEffectTip);
		
		new PdfBatchPrintWriter(PageSize.A5.getWidth(), PageSize.A5.getHeight(), WAR_PATH + "\\病假单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 35f, 10f, 25f, 68f, 44f, 50f, 44f, 35f, 70f, 13f, 90f)
		.endPage();
	}
	
	@Test
	//注射单  、、120 80
	public void printInjectionTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData injectionTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 80, 11, null, 30, "广东技术师范学院门诊部\n注射单");
		injectionTitleData.setFontspace(50);
		printList.add(injectionTitleData);
		
		PdfCellPrintData injectionIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		injectionIdTip.setFontspace(10);
		printList.add(injectionIdTip);
		PdfCellPrintData injectionTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		injectionTreatmentTip.setFontspace(10);
		printList.add(injectionTreatmentTip);
		
		PdfCellPrintData injectionNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 司马张全张全");
		injectionNameTip.setFontspace(10);
		printList.add(injectionNameTip);
		PdfCellPrintData injectionSexTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		injectionSexTip.setFontspace(10);
		printList.add(injectionSexTip);
		PdfCellPrintData injectionAgeTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 123");
		injectionAgeTip.setFontspace(10);
		printList.add(injectionAgeTip);
		PdfCellPrintData injectionCostTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "人员费别: 学生学生10%");
		injectionCostTip.setFontspace(10);
		printList.add(injectionCostTip);
		
		PdfCellPrintData injectionFreePayTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "医保: 有");
		injectionFreePayTip.setFontspace(10);
		printList.add(injectionFreePayTip);
		PdfCellPrintData injectionHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "工作部门/所在班级: 14级服装设计1班");
		injectionHistoryTip.setFontspace(10);
		printList.add(injectionHistoryTip);
		PdfCellPrintData injectionIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "开具日期: 2016年12月29号");
		injectionIssueDateTip.setFontspace(10);
		printList.add(injectionIssueDateTip);
		
		PdfCellPrintData injectionTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "注射用法");
		injectionTip.setFontspace(10);
		printList.add(injectionTip);
		PdfCellPrintData injectionText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 260, 11, null, 10, "5mg 阿拉明注射液 + 生理盐水 500ml/次 一日两次");
		injectionText.setFontspace(10);
		printList.add(injectionText);
		
		PdfCellPrintData injectionNoteCardTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "注射记录卡");
		injectionNoteCardTip.setFontspace(10);
		printList.add(injectionNoteCardTip);
		
		PdfCellPrintData injectionDateTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "处理日期");
		injectionDateTip.setFontspace(10);
		printList.add(injectionDateTip);
		PdfCellPrintData injectionDateText1 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionDateText1.setFontspace(10);
		printList.add(injectionDateText1);
		PdfCellPrintData injectionDateText2 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionDateText2.setFontspace(10);
		printList.add(injectionDateText2);		
		PdfCellPrintData injectionDateText3 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionDateText3.setFontspace(10);
		printList.add(injectionDateText3);		
		PdfCellPrintData injectionDateText4 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionDateText4.setFontspace(10);
		printList.add(injectionDateText4);
		PdfCellPrintData injectionDateText5 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionDateText5.setFontspace(10);
		printList.add(injectionDateText5);
		
		PdfCellPrintData injectionImplementTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "执行护士");
		injectionImplementTip.setFontspace(10);
		printList.add(injectionImplementTip);
		PdfCellPrintData injectionImplementText1 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionImplementText1.setFontspace(10);
		printList.add(injectionImplementText1);
		PdfCellPrintData injectionImplementText2 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionImplementText2.setFontspace(10);
		printList.add(injectionImplementText2);
		PdfCellPrintData injectionImplementText3 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionImplementText3.setFontspace(10);
		printList.add(injectionImplementText3);
		PdfCellPrintData injectionImplementText4 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		injectionImplementText4.setFontspace(10);
		printList.add(injectionImplementText4);
		PdfCellPrintData injectionImplementText5 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		injectionImplementText5.setFontspace(10);
		printList.add(injectionImplementText5);

		PdfCellPrintData injectionDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 5, null, 10, "接诊医生: Admin");
		injectionDoctorTip.setFontspace(10);
		printList.add(injectionDoctorTip);
		PdfCellPrintData injectionSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "医师（签章）:");
		injectionSignatureTip.setFontspace(10);
		printList.add(injectionSignatureTip);
		PdfCellPrintData injectionPriceTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "治疗费用: ￥32210.0");
		injectionPriceTip.setFontspace(10);
		printList.add(injectionPriceTip);
		
		new PdfBatchPrintWriter(PageSize.A5.getWidth(), PageSize.A5.getHeight(), WAR_PATH + "\\注射单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 35f, 10f, 25f, 68f, 44f, 50f, 44f, 35f, 70f, 13f, 90f)
		.endPage();
	}
	
	@Test
	//换药单
	public void printExchangeMedicineTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData exchangeTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 80, 11, null, 30, "广东技术师范学院门诊部\n换药单");
		exchangeTitleData.setFontspace(50);
		printList.add(exchangeTitleData);
		
		PdfCellPrintData exchangeIdTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 8, null, 10, "医保卡号/校医疗证号: 440711100096382736");
		exchangeIdTip.setFontspace(10);
		printList.add(exchangeIdTip);
		PdfCellPrintData exchangeTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "诊断流水号: 12345678");
		exchangeTreatmentTip.setFontspace(10);
		printList.add(exchangeTreatmentTip);	
		
		PdfCellPrintData exchangeNameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 4, null, 10, "姓名: 司马张全张全");
		exchangeNameTip.setFontspace(10);
		printList.add(exchangeNameTip);
		PdfCellPrintData exchangeSexTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "性别: 男");
		exchangeSexTip.setFontspace(10);
		printList.add(exchangeSexTip);
		PdfCellPrintData exchangeAgeTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 2, null, 10, "年龄: 133");
		exchangeAgeTip.setFontspace(10);
		printList.add(exchangeAgeTip);
		PdfCellPrintData exchangeCostTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 25, 3, null, 10, "人员费别: 学生学生10%");
		exchangeCostTip.setFontspace(10);
		printList.add(exchangeCostTip);
		
		PdfCellPrintData exchangeFreePayTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "医保: 有");
		exchangeFreePayTip.setFontspace(10);
		printList.add(exchangeFreePayTip);
		PdfCellPrintData exchangeHistoryTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 5, null, 10, "工作部门/所在班级: 14级服装设计1班");
		exchangeHistoryTip.setFontspace(10);
		printList.add(exchangeHistoryTip);
		PdfCellPrintData exchangeIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 3, null, 10, "开具日期: 2016年12月29号");
		exchangeIssueDateTip.setFontspace(10);
		printList.add(exchangeIssueDateTip);
		
		PdfCellPrintData exchangeWayTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "换药用法");
		exchangeWayTip.setFontspace(10);
		printList.add(exchangeWayTip);
		PdfCellPrintData exchangeWayText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_THICK, 250, 11, null, 10, "换药部位:手臂 \n换药范围大小:创面、伤口冲洗\n换药次数:一日两次\n换药方法:0.1-0.2%纱布");
		exchangeWayText.setFontspace(10);
		printList.add(exchangeWayText);
		PdfCellPrintData exchangeNoteCardTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 11, null, 10, "换药记录卡");
		exchangeNoteCardTip.setFontspace(10);
		printList.add(exchangeNoteCardTip);
		
		PdfCellPrintData exchangeDateTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "处理日期");
		exchangeDateTip.setFontspace(10);
		printList.add(exchangeDateTip);
		PdfCellPrintData exchangeDateText1 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeDateText1.setFontspace(10);
		printList.add(exchangeDateText1);
		PdfCellPrintData exchangeDateText2 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeDateText2.setFontspace(10);
		printList.add(exchangeDateText2);		
		PdfCellPrintData exchangeDateText3 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeDateText3.setFontspace(10);
		printList.add(exchangeDateText3);		
		PdfCellPrintData exchangeDateText4 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeDateText4.setFontspace(10);
		printList.add(exchangeDateText4);
		PdfCellPrintData exchangeDateText5 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeDateText5.setFontspace(10);
		printList.add(exchangeDateText5);
		
		PdfCellPrintData exchangeImplementTip = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 3, null, 10, "执行护士");
		exchangeImplementTip.setFontspace(10);
		printList.add(exchangeImplementTip);
		PdfCellPrintData exchangeImplementText1 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeImplementText1.setFontspace(10);
		printList.add(exchangeImplementText1);
		PdfCellPrintData exchangeImplementText2 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeImplementText2.setFontspace(10);
		printList.add(exchangeImplementText2);
		PdfCellPrintData exchangeImplementText3 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeImplementText3.setFontspace(10);
		printList.add(exchangeImplementText3);
		PdfCellPrintData exchangeImplementText4 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 2, null, 10, "");
		exchangeImplementText4.setFontspace(10);
		printList.add(exchangeImplementText4);
		PdfCellPrintData exchangeImplementText5 = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.THIN, 30, 1, null, 10, "");
		exchangeImplementText5.setFontspace(10);
		printList.add(exchangeImplementText5);
		
		PdfCellPrintData exchangeDoctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 5, null, 10, "接诊医生: Admin");
		exchangeDoctorTip.setFontspace(10);
		printList.add(exchangeDoctorTip);
		PdfCellPrintData exchangeSignatureTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "医师（签章）:");
		exchangeSignatureTip.setFontspace(10);
		printList.add(exchangeSignatureTip);
		PdfCellPrintData exchangePriceTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.BOTTOM, BorderMode.NONE, 30, 3, null, 10, "治疗费用: ￥2310.0");
		exchangePriceTip.setFontspace(10);
		printList.add(exchangePriceTip);
		
		new PdfBatchPrintWriter(PageSize.A5.getWidth(), PageSize.A5.getHeight(), WAR_PATH + "\\换药单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 35f, 10f, 25f, 68f, 44f, 50f, 44f, 35f, 70f, 13f, 90f)
		.endPage();
	}
	
	@Test
	//收费单
	public void printChargeTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 8, null, 17, 0f, 0f, "广东技术师范学院门诊部\n收费单");
		titleData.setFontspace(50);
		printList.add(titleData);
		
		PdfCellPrintData nameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 18, 3, null, 7, 0f, 0f, "姓名: 张全蛋张全蛋");
		nameTip.setFontspace(10);
		printList.add(nameTip);
		PdfCellPrintData costTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 18, 3, null, 7, 0f, 0f, "人员费别: 学生学生100%");
		costTip.setFontspace(10);
		printList.add(costTip);
		PdfCellPrintData treatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 18, 2, null, 7, 0f, 0f, "诊断流水号: 12345678");
		treatmentTip.setFontspace(10);
		printList.add(treatmentTip);
		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 3, null, 10, 0f, 0f, "收费项目"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 2, null, 10, 0f, 0f, "费用金额"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 2, null, 10, 0f, 0f, "收费比率"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 1, null, 10, 0f, 0f, "应收金额"));
		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 3, null, 10, 0f, 0f, "医保药品费"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "20.0"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "10%"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 1, null, 10, 0f, 0f, "￥2.0"));

		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 3, null, 10, 0f, 0f, "非医保药品费"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "22.0"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "10%"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 1, null, 10, 0f, 0f, "￥2.0"));
		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 3, null, 10, 0f, 0f, "注射费"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "22.0"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "10%"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 1, null, 10, 0f, 0f, "￥2.0"));

		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 3, null, 10, 0f, 0f, "冲挂冲抵费"));		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "22.0"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 2, null, 10, 0f, 0f, "10%"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 1, null, 10, 0f, 0f, "￥2.0"));
		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 3, null, 10, 0f, 0f, "换药费"));		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 2, null, 10, 0f, 0f, "22.0"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 2, null, 10, 0f, 0f, "10%"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 1, null, 10, 0f, 0f, "￥2.0"));
	
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 7, null, 10, 0f, 0f, "费用总计"));	
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 25, 1, null, 10, 0f, 0f, "￥2.0"));	

		PdfCellPrintData employerTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.TOP, BorderMode.BOTTOM_DASH, 25, 4, null, 10, 5f, 0f, "收费员: 张三");
		employerTip.setFontspace(10);
		printList.add(employerTip);
		PdfCellPrintData issueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.TOP, BorderMode.BOTTOM_DASH, 25, 4, null, 10, 5f, 0f, "开具日期: 2016年12月24号");
		issueDateTip.setFontspace(10);
		printList.add(issueDateTip);

		
		PdfCellPrintData goodsTitleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.BOTTOM, BorderMode.NONE, 35, 8, null, 17, 0f, 0f, "广东技术师范学院门诊部\n药品清单");
		goodsTitleData.setFontspace(50);
		printList.add(goodsTitleData);
		
		PdfCellPrintData nameTip1 = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 15, 3, null, 10, 0f, 0f, "姓名: 张全蛋张全蛋");
		nameTip1.setFontspace(10);
		printList.add(nameTip1);
		PdfCellPrintData goodsCostTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 15, 3, null, 10, 0f, 0f, "人员费别: 学生学生10%");
		goodsCostTip.setFontspace(10);
		printList.add(goodsCostTip);
		PdfCellPrintData goodsTreatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 15, 2, null, 10, 0f, 0f, "诊断流水号: 12345678");
		goodsTreatmentTip.setFontspace(10);
		printList.add(goodsTreatmentTip);
		
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 4, null, 10, 0f, 0f, "药品名称"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 1, null, 10, 0f, 0f, "数量"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 2, null, 10, 0f, 0f, "医保药品"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 1, null, 10, 0f, 0f, "费用金额"));

		int i = 5;
		while (i>0) {
			i--;
			printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 20, 4, null, 10, 0f, 0f, "百灵C银翘片银百灵C银翘片银翘片（24片/盒）"));
			printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 20, 1, null, 10, 0f, 0f, "2"));
			printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 20, 2, null, 10, 0f, 0f, "是"));
			printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 20, 1, null, 10, 0f, 0f, "￥2.0"));
		}

		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 7, null, 10, 0f, 0f, "费用总计"));
		printList.add(new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THIN, 15, 1, null, 10, 0f, 0f, "￥222.0"));
		
		PdfCellPrintData doctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 4, null, 10, 0f, 0f, "接诊医生: 张三");
		doctorTip.setFontspace(10);
		printList.add(doctorTip);
		PdfCellPrintData goodsIssueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 15, 4, null, 10, 0f, 0f, "开具日期: 2016年12月22号");
		goodsIssueDateTip.setFontspace(10);
		printList.add(goodsIssueDateTip);
		
		
		new PdfBatchPrintWriter(333-33, 256, WAR_PATH + "\\收费单.pdf", null, Zoom.DEFAULT) //276-20
		.gridLayerPrint(printList, 40f, 20f, 30f, 30f, 40f, 30f, 40f, 60f) 
		.endPage();
	}
	
	@Test
	//药品清单
	public void printGoodsListTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 60, 6, null, 22, "广东技术师范学院门诊部\n药品清单");
		titleData.setFontspace(50);
		printList.add(titleData);
		
		PdfCellPrintData nameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "姓名:");
		nameTip.setFontspace(10);
		printList.add(nameTip);
		PdfCellPrintData nameText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "张全蛋");
		nameText.setFontspace(10);
		printList.add(nameText);
		PdfCellPrintData costTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "人员费别:");
		costTip.setFontspace(10);
		printList.add(costTip);
		PdfCellPrintData costText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "学生10%");
		costText.setFontspace(10);
		printList.add(costText);
		PdfCellPrintData treatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "诊断流水号:");
		treatmentTip.setFontspace(10);
		printList.add(treatmentTip);
		PdfCellPrintData treatmentNum = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "12345678");
		treatmentNum.setFontspace(10);
		printList.add(treatmentNum);	
		
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 3, "药品名称"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "数量"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "医保药品"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "费用金额"));
	
		printList.add(new PdfCellPrintData(BorderMode.NONE, 3, "百灵C银翘片（24片/盒）"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "2"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "是"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "￥2.0"));

		printList.add(new PdfCellPrintData(BorderMode.NONE, 3, "百灵C银翘片（24片/盒）"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "2"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "是"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "￥2.0"));

		printList.add(new PdfCellPrintData(BorderMode.NONE, 3, "百灵C银翘片（24片/盒）"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "2"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "是"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "￥2.0"));	
		
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 3, "百灵C银翘片（24片/盒）"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "2"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "是"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "￥2.0"));

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 3, "费用总计"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 1, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 1, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 1, "￥222.0"));
		
		PdfCellPrintData doctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 1, null, 10, "接诊医生:");
		doctorTip.setFontspace(10);
		printList.add(doctorTip);
		PdfCellPrintData doctorText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 1, null, 10, "张三");
		doctorText.setFontspace(10);
		printList.add(doctorText);
		PdfCellPrintData issueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 2, null, 10, "开具日期:");
		issueDateTip.setFontspace(10);
		printList.add(issueDateTip);
		PdfCellPrintData issueDateText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 2, null, 10, "2016年9月2号");
		issueDateText.setFontspace(10);
		printList.add(issueDateText);
		
		new PdfBatchPrintWriter(461, 353, WAR_PATH + "\\药品清单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 85f, 70f, 65f, 65f, 65f, 63f)
		.endPage();
	}
	
	@Test
	//注射清单
	public void printInjectionListTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 60, 6, null, 22, "广东技术师范学院门诊部\n注射清单");
		titleData.setFontspace(50);
		printList.add(titleData);
		
		PdfCellPrintData nameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "姓名:");
		nameTip.setFontspace(10);
		printList.add(nameTip);
		PdfCellPrintData nameText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "张全蛋");
		nameText.setFontspace(10);
		printList.add(nameText);
		PdfCellPrintData costTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "人员费别:");
		costTip.setFontspace(10);
		printList.add(costTip);
		PdfCellPrintData costText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "学生10%");
		costText.setFontspace(10);
		printList.add(costText);
		PdfCellPrintData treatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "诊断流水号:");
		treatmentTip.setFontspace(10);
		printList.add(treatmentTip);
		PdfCellPrintData treatmentNum = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "12345678");
		treatmentNum.setFontspace(10);
		printList.add(treatmentNum);	
		
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 3, "物品名称"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "数量"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "医保药品"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "费用金额"));

		
		printList.add(new PdfCellPrintData(BorderMode.NONE, 3, "百灵C银翘片（24片/盒）"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "2"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "是"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, 1, "￥2.0"));
	
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 3, "百灵C银翘片（24片/盒）"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "2"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "否"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, 1, "￥2.0"));

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 3, "费用总计"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 1, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 1, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, 1, "￥222.0"));
		
		PdfCellPrintData doctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 1, null, 10, "接诊医生:");
		doctorTip.setFontspace(10);
		printList.add(doctorTip);
		PdfCellPrintData doctorText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 1, null, 10, "张三");
		doctorText.setFontspace(10);
		printList.add(doctorText);
		PdfCellPrintData issueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 2, null, 10, "开具日期:");
		issueDateTip.setFontspace(10);
		printList.add(issueDateTip);
		PdfCellPrintData issueDateText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 2, null, 10, "2016年9月2号");
		issueDateText.setFontspace(10);
		printList.add(issueDateText);
		
		new PdfBatchPrintWriter(461, 353, WAR_PATH + "\\注射清单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 85f, 70f, 65f, 65f, 65f, 63f)
		.endPage();
	}
	/*	
	@Test
	//换药清单
	public void printExchangeMedicialListTable(){
		List<PdfCellPrintData> printList = new ArrayList<>();
		PdfCellPrintData titleData = new PdfCellPrintData(HorizontalAlign.CENTER, VerticalAlign.MIDDLE, BorderMode.NONE, 60, 6, null, 22, "广东技术师范学院门诊部\n换药清单");
		titleData.setFontspace(50);
		printList.add(titleData);
		
		PdfCellPrintData nameTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "姓名:");
		nameTip.setFontspace(10);
		printList.add(nameTip);
		PdfCellPrintData nameText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "张全蛋");
		nameText.setFontspace(10);
		printList.add(nameText);
		PdfCellPrintData costTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "人员费别:");
		costTip.setFontspace(10);
		printList.add(costTip);
		PdfCellPrintData costText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "学生10%");
		costText.setFontspace(10);
		printList.add(costText);
		PdfCellPrintData treatmentTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "诊断流水号:");
		treatmentTip.setFontspace(10);
		printList.add(treatmentTip);
		PdfCellPrintData treatmentNum = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.BOTTOM_THICK, 25, 1, null, 10, "12345678");
		treatmentNum.setFontspace(10);
		printList.add(treatmentNum);	
		
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "物品名称"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "数量"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "医保药品"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "费用金额"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, ""));
		
		printList.add(new PdfCellPrintData(BorderMode.NONE, null, "百灵C银翘片"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, null, "2"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, null, "是"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, null, "￥2.0"));
		printList.add(new PdfCellPrintData(BorderMode.NONE, null, ""));
		printList.add(new PdfCellPrintData(BorderMode.NONE, null, ""));
	
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "百灵C银翘片"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "2"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "否"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, "￥2.0"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THIN, null, ""));

		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "费用总计"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, "￥222.0"));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, ""));
		printList.add(new PdfCellPrintData(BorderMode.BOTTOM_THICK, null, ""));
		
		PdfCellPrintData doctorTip = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 1, null, 10, "接诊医生:");
		doctorTip.setFontspace(10);
		printList.add(doctorTip);
		PdfCellPrintData doctorText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 1, null, 10, "张三");
		doctorText.setFontspace(10);
		printList.add(doctorText);
		PdfCellPrintData issueDateTip = new PdfCellPrintData(HorizontalAlign.RIGHT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 2, null, 10, "开具日期:");
		issueDateTip.setFontspace(10);
		printList.add(issueDateTip);
		PdfCellPrintData issueDateText = new PdfCellPrintData(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, BorderMode.NONE, 35, 2, null, 10, "2016年9月2号");
		issueDateText.setFontspace(10);
		printList.add(issueDateText);
		
		new PdfBatchPrintWriter(461, 353, WAR_PATH + "\\换药清单.pdf", null, Zoom.DEFAULT)
		.gridLayerPrint(printList, 85f, 70f, 65f, 65f, 65f, 63f)
		.endPage();
	}
*/
}
