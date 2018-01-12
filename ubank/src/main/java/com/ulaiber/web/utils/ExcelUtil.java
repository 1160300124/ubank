package com.ulaiber.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.ulaiber.web.model.salary.SalaryDetail;

/**
 * Excel工具类
 * 
 * @author huangguoqing
 *
 */
public class ExcelUtil {

	private static Logger logger = Logger.getLogger(ExcelUtil.class); 

	public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";  
	public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";  
	public static final String EMPTY = "";  
	public static final String POINT = ".";  
	public static SimpleDateFormat sdf =   new SimpleDateFormat("yyyy/MM/dd");  

	/** 
	 * 获得path的后缀名 
	 * @param path 
	 * @return 
	 */  
	private static String getPostfix(String path){  
		if(path==null || EMPTY.equals(path.trim())){  
			return EMPTY;  
		}  
		if(path.contains(POINT)){  
			return path.substring(path.lastIndexOf(POINT)+1,path.length());  
		}  
		return EMPTY;  
	}  

	/** 
	 * 单元格格式 
	 * @param hssfCell 
	 * @return 
	 */  
	private static String getHValue(HSSFCell hssfCell){
		if (hssfCell == null){
			return "";
		}
		if (hssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {  
			return String.valueOf(hssfCell.getBooleanCellValue());  
		} else if (hssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {  
			String cellValue = "";  
			if(HSSFDateUtil.isCellDateFormatted(hssfCell)){                  
				Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());  
				cellValue = sdf.format(date);  
			}else{  
				DecimalFormat df = new DecimalFormat("#.##");  
				cellValue = df.format(hssfCell.getNumericCellValue());  
				String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());  
				if(strArr.equals("00")){  
					cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));  
				}    
			}  
			return cellValue;  
		} else {  
			return String.valueOf(hssfCell.getStringCellValue());  
		}  
	}  

	/** 
	 * 单元格格式 
	 * @param xssfCell 
	 * @return 
	 */  
	private static String getXValue(XSSFCell xssfCell){  
		if (xssfCell == null){
			return "";
		}
		if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {  
			return String.valueOf(xssfCell.getBooleanCellValue());  
		} else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {  
			String cellValue = "";  
			if(XSSFDateUtil.isCellDateFormatted(xssfCell)){  
				Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());  
				cellValue = sdf.format(date);  
			}else{  
				DecimalFormat df = new DecimalFormat("#.##");  
				cellValue = df.format(xssfCell.getNumericCellValue());  
				String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());  
				if(strArr.equals("00")){  
					cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));  
				}    
			}  
			return cellValue;  
		} else {  
			return String.valueOf(xssfCell.getStringCellValue());  
		}  
	}     

	/** 
	 * 自定义xssf日期工具类 
	 * @author lp 
	 * 
	 */  
	static class XSSFDateUtil extends DateUtil{  
		protected static int absoluteDay(Calendar cal, boolean use1904windowing) {    
			return DateUtil.absoluteDay(cal, use1904windowing);    
		}   
	}

	/** 
	 * read the Excel .xlsx,.xls 
	 * @param file jsp中的上传文件 
	 * @return 
	 * @throws IOException  
	 */  
	public static Map<String, Object> readExcel(MultipartFile file, List<String> cardNoList) throws IOException {  
		if (file==null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())){  
			return null;  
		} else {  
			String postfix = ExcelUtil.getPostfix(file.getOriginalFilename());  
			if (!ExcelUtil.EMPTY.equals(postfix)) {  
				if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {  
					return readXls(file, cardNoList);  
				} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {  
					return readXlsx(file, cardNoList);  
				} else {                    
					return null;  
				}  
			}  
		}  
		return null;  
	} 

	/** 
	 * read the Excel 2010 .xlsx 
	 * @param file 
	 * @param beanclazz 
	 * @param titleExist 
	 * @return 
	 * @throws IOException  
	 */  
	private static Map<String, Object> readXlsx(MultipartFile file, List<String> cardNoList){ 
		Map<String, Object> data = new HashMap<String, Object>();
		//EXCEL工资表集合
		List<SalaryDetail> details = new ArrayList<SalaryDetail>();
		//EXCEL工资表错误集合
		List<SalaryDetail> failDetails = new ArrayList<SalaryDetail>();
		//EXCEL工资表正确集合
		List<SalaryDetail> successDetails = new ArrayList<SalaryDetail>();
		//身份证集合
		Set<String> cardNoSet = new HashSet<String>();
		// IO流读取文件  
		InputStream input = null;  
		XSSFWorkbook wb = null;  
		try {  
			input = file.getInputStream();  
			// 创建文档  
			wb = new XSSFWorkbook(input);                         
			//读取sheet(页)  
			for(int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++){  
				XSSFSheet xssfSheet = wb.getSheetAt(numSheet);  
				if(xssfSheet == null){  
					continue;  
				}  
				//读取Row,从第1行开始  
				for(int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++){  
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);  
					if (xssfRow != null){  
						//第一行是表头，跳过
						if (rowNum == 0){
							continue;
						}
						SalaryDetail detail = new SalaryDetail();
						detail.setEid(rowNum + 1);

						StringBuffer sb = new StringBuffer();
						//读取列，从第一列开始  
						//0姓名  1身份证号码  2税前工资  3补贴	4奖金  5考勤扣款  6请假扣款  7加班费  8社保缴纳  9公积金  10个税起征点  11个人所得税  12其他扣款  13应发工资
						if (StringUtils.isEmpty(getXValue(xssfRow.getCell(0)))){
							sb.append("姓名不能为空,");
						}
						detail.setUserName(getXValue(xssfRow.getCell(0)));

						if (StringUtils.isEmpty(getXValue(xssfRow.getCell(1)))){
							sb.append("身份证号码不能为空,");
						} else {
							if (!cardNoList.contains(getXValue(xssfRow.getCell(1)))){
								sb.append("没有对应员工,");
							} else {
								if (cardNoSet.contains(getXValue(xssfRow.getCell(1)))){
									sb.append("该条记录重复了,");
								}
								cardNoSet.add(getXValue(xssfRow.getCell(1)));
							}
						}
						detail.setCardNo(getXValue(xssfRow.getCell(1)));

						if (StringUtils.isEmpty(getXValue(xssfRow.getCell(2)))){
							sb.append("税前工资不能为空,");
							detail.setPreTaxSalaries(0);
						} else {
							detail.setPreTaxSalaries(Double.valueOf(getXValue(xssfRow.getCell(2))));
						}

						if (StringUtils.isEmpty(getXValue(xssfRow.getCell(13)))){
							sb.append("应发工资不能为空,");
							detail.setSalaries(0);
						} else {
							detail.setSalaries(Double.valueOf(getXValue(xssfRow.getCell(13))));
						}

						detail.setRemark(sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1));
						detail.setBonuses(StringUtils.isEmpty(getXValue(xssfRow.getCell(3))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(3))));
						detail.setSubsidies(StringUtils.isEmpty(getXValue(xssfRow.getCell(4))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(4))));
						detail.setTotalCutPayment(StringUtils.isEmpty(getXValue(xssfRow.getCell(5))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(5))));
						detail.setAskForLeaveCutPayment(StringUtils.isEmpty(getXValue(xssfRow.getCell(6))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(6))));
						detail.setOvertimePayment(StringUtils.isEmpty(getXValue(xssfRow.getCell(7))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(7))));
						detail.setSocialInsurance(StringUtils.isEmpty(getXValue(xssfRow.getCell(8))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(8))));
						detail.setPublicAccumulationFunds(StringUtils.isEmpty(getXValue(xssfRow.getCell(9))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(9))));
						detail.setTaxThreshold(StringUtils.isEmpty(getXValue(xssfRow.getCell(10))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(10))));
						detail.setPersonalIncomeTax(StringUtils.isEmpty(getXValue(xssfRow.getCell(11))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(11))));
						detail.setElseCutPayment(StringUtils.isEmpty(getXValue(xssfRow.getCell(12))) ? 0 : Double.valueOf(getXValue(xssfRow.getCell(12))));

						if (sb.length() == 0){
							successDetails.add(detail);
						} else {
							failDetails.add(detail);
						}
					}  
				}  
			}  

		} catch (IOException e) {             
			logger.error("readXlsx exception:", e);
		} finally{  
			try {
				if (input != null){
					input.close();  
				}
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		
		details.addAll(failDetails);
		details.addAll(successDetails);
		data.put("details", details);
		data.put("failCount", failDetails.size());
		data.put("successCount", successDetails.size());
		return data;
	}

	/** 
	 * read the Excel 2003-2007 .xls 
	 * @param file 
	 * @param beanclazz 
	 * @param titleExist 
	 * @return 
	 * @throws IOException  
	 */  
	private static Map<String, Object> readXls(MultipartFile file, List<String> cardNoList){
		Map<String, Object> data = new HashMap<String, Object>();
		//EXCEL工资表集合
		List<SalaryDetail> details = new ArrayList<SalaryDetail>();
		//EXCEL工资表错误集合
		List<SalaryDetail> failDetails = new ArrayList<SalaryDetail>();
		//EXCEL工资表正确集合
		List<SalaryDetail> successDetails = new ArrayList<SalaryDetail>();
		//身份证集合
		Set<String> cardNoSet = new HashSet<String>();
		// IO流读取文件  
		InputStream input = null;  
		HSSFWorkbook wb = null;  
		try {  
			input = file.getInputStream();  
			// 创建文档  
			wb = new HSSFWorkbook(input);                         
			//读取sheet(页)  
			for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++){  
				HSSFSheet hssfSheet = wb.getSheetAt(numSheet);  
				if(hssfSheet == null){  
					continue;  
				}  
				//读取Row,从第1行开始
				for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++){  
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow != null){
						//第一行是表头，跳过
						if (rowNum == 0){
							continue;
						}
						SalaryDetail detail = new SalaryDetail();
						detail.setEid(rowNum + 1);

						StringBuffer sb = new StringBuffer();
						//读取列，从第一列开始  
						//0姓名  1身份证号码  2税前工资  3补贴	4奖金  5考勤扣款  6请假扣款  7加班费  8社保缴纳  9公积金  10个税起征点  11个人所得税  12其他扣款  13应发工资
						if (StringUtils.isEmpty(getHValue(hssfRow.getCell(0)))){
							sb.append("姓名不能为空,");
						}
						detail.setUserName(getHValue(hssfRow.getCell(0)));

						if (StringUtils.isEmpty(getHValue(hssfRow.getCell(1)))){
							sb.append("身份证号码不能为空,");
						} else {
							if (!cardNoList.contains(getHValue(hssfRow.getCell(1)))){
								sb.append("没有对应员工,");
							} else {
								cardNoSet.add(getHValue(hssfRow.getCell(1)));
								if (cardNoSet.contains(getHValue(hssfRow.getCell(1)))){
									sb.append("该条记录重复了,");
								}
							}
						}
						detail.setCardNo(getHValue(hssfRow.getCell(1)));

						if (StringUtils.isEmpty(getHValue(hssfRow.getCell(2)))){
							sb.append("税前工资不能为空,");
							detail.setPreTaxSalaries(0);
						} else {
							detail.setPreTaxSalaries(Double.valueOf(getHValue(hssfRow.getCell(2))));
						}

						if (StringUtils.isEmpty(getHValue(hssfRow.getCell(13)))){
							sb.append("应发工资不能为空,");
							detail.setSalaries(0);
						} else {
							detail.setSalaries(Double.valueOf(getHValue(hssfRow.getCell(13))));
						}

						detail.setRemark(sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1));
						detail.setBonuses(StringUtils.isEmpty(getHValue(hssfRow.getCell(3))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(3))));
						detail.setSubsidies(StringUtils.isEmpty(getHValue(hssfRow.getCell(4))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(4))));
						detail.setTotalCutPayment(StringUtils.isEmpty(getHValue(hssfRow.getCell(5))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(5))));
						detail.setAskForLeaveCutPayment(StringUtils.isEmpty(getHValue(hssfRow.getCell(6))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(6))));
						detail.setOvertimePayment(StringUtils.isEmpty(getHValue(hssfRow.getCell(7))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(7))));
						detail.setSocialInsurance(StringUtils.isEmpty(getHValue(hssfRow.getCell(8))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(8))));
						detail.setPublicAccumulationFunds(StringUtils.isEmpty(getHValue(hssfRow.getCell(9))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(9))));
						detail.setTaxThreshold(StringUtils.isEmpty(getHValue(hssfRow.getCell(10))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(10))));
						detail.setPersonalIncomeTax(StringUtils.isEmpty(getHValue(hssfRow.getCell(11))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(11))));
						detail.setElseCutPayment(StringUtils.isEmpty(getHValue(hssfRow.getCell(12))) ? 0 : Double.valueOf(getHValue(hssfRow.getCell(12))));
						
						if (sb.length() == 0){
							successDetails.add(detail);
						} else {
							failDetails.add(detail);
						}
					}                     
				}  
			}  

		} catch (IOException e) {             
			logger.error("readXls exception:", e);
		} finally{  
			try {
				if (input != null){
					input.close();  
				}
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		
		details.addAll(failDetails);
		details.addAll(successDetails);
		data.put("details", details);
		data.put("failCount", failDetails.size());
		data.put("successCount", successDetails.size());
		return data; 
	}  

}