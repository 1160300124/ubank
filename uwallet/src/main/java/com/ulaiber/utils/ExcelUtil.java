package com.ulaiber.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.ulaiber.model.SalaryDetail;
import com.ulaiber.model.Salary;

/**
 * Excel工具类
 * 
 * @author huangguoqing
 *
 */
public class ExcelUtil {
	
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
	public static String getPostfix(String path){  
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
	@SuppressWarnings({ "static-access" })  
	public static String getHValue(HSSFCell hssfCell){  
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {  
			return String.valueOf(hssfCell.getBooleanCellValue());  
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {  
			String cellValue = "";  
			if(HSSFDateUtil.isCellDateFormatted(hssfCell)){                  
				Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());  
				cellValue = sdf.format(date);  
			}else{  
				DecimalFormat df = new DecimalFormat("#.##");  
				cellValue = df.format(hssfCell.getNumericCellValue());  
				String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());  
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
	public static String getXValue(XSSFCell xssfCell){  
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
				String strArr = cellValue.substring(cellValue.lastIndexOf(POINT)+1,cellValue.length());  
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
    public static Map<String, Object> readExcel(MultipartFile file) throws IOException {  
    	if (file==null || ExcelUtil.EMPTY.equals(file.getOriginalFilename().trim())){  
    		return null;  
    	} else {  
    		String postfix = ExcelUtil.getPostfix(file.getOriginalFilename());  
    		if (!ExcelUtil.EMPTY.equals(postfix)) {  
    			if (ExcelUtil.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {  
    				return readXls(file);  
    			} else if (ExcelUtil.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {  
    				return readXlsx(file);  
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
    public static Map<String, Object> readXlsx(MultipartFile file){ 
    	Map<String, Object> map = new HashMap<String, Object>();
        List<SalaryDetail> details = new ArrayList<SalaryDetail>();  
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
                //读取Row,从第3行开始  
                for(int rowNum = 2; rowNum <= xssfSheet.getLastRowNum(); rowNum++){  
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);  
                    if(xssfRow != null){  
                       //读取列，从第一列开始  
                    	if (rowNum == 2){
                    		Salary sa = new Salary();
                    		sa.setTotalNumber(Integer.valueOf(getXValue(xssfRow.getCell(1))));
                    		sa.setTotalAmount(Double.valueOf(getXValue(xssfRow.getCell(3))));
                    		sa.setSalaryDate(getXValue(xssfRow.getCell(4)));
                    		map.put("sa", sa);
                    		continue;
                    	}
                    	
                    	//第四行是表头，跳过
                    	if (rowNum == 3){
                    		continue;
                    	}
                    	
                        if (StringUtils.isEmpty(getXValue(xssfRow.getCell(0))) || StringUtils.isEmpty(getXValue(xssfRow.getCell(3)))
                        		|| StringUtils.isEmpty(getXValue(xssfRow.getCell(2))) || StringUtils.isEmpty(getXValue(xssfRow.getCell(3)))){
                        	break;
                        }
                        
                        SalaryDetail detail = new SalaryDetail();
                        detail.setEid(getXValue(xssfRow.getCell(0)));
                        detail.setUserName(getXValue(xssfRow.getCell(1)));
                        detail.setCardNo(getXValue(xssfRow.getCell(0)));
                        detail.setSalaries(Double.valueOf(getXValue(xssfRow.getCell(0))));
                        detail.setRemark(getXValue(xssfRow.getCell(0)));
                        detail.setSalaryDate(DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_SHORTDAY));
                        details.add(detail);                                          
                    }  
                }  
            }  
            map.put("details", details);
            return map;  
        } catch (IOException e) {             
            e.printStackTrace();  
        } finally{  
            try {
            	if (input != null){
            		input.close();  
            	}
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
          
    }
    
    /** 
     * read the Excel 2003-2007 .xls 
     * @param file 
     * @param beanclazz 
     * @param titleExist 
     * @return 
     * @throws IOException  
     */  
    public static Map<String, Object> readXls(MultipartFile file){
    	Map<String, Object> map = new HashMap<String, Object>();
    	List<SalaryDetail> details = new ArrayList<SalaryDetail>();
    	
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
                //读取Row,从第3行开始,第三行是总笔数，总金额，发放时间
                for (int rowNum = 2; rowNum <= hssfSheet.getLastRowNum(); rowNum++){  
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if (hssfRow != null){  
                    	if (rowNum == 2){
                    		Salary sa = new Salary();
                    		sa.setTotalNumber(Integer.valueOf(getHValue(hssfRow.getCell(1))));
                    		sa.setTotalAmount(Double.valueOf(getHValue(hssfRow.getCell(3))));
                    		sa.setSalaryDate(getHValue(hssfRow.getCell(4)));
                    		map.put("sa", sa);
                    		continue;
                    	}
                    	
                    	//第四行是表头，跳过
                    	if (rowNum == 3){
                    		continue;
                    	}
                    	
                    	if (StringUtils.isEmpty(getHValue(hssfRow.getCell(0)))|| StringUtils.isEmpty(getHValue(hssfRow.getCell(1)))
                    			|| StringUtils.isEmpty(getHValue(hssfRow.getCell(2))) || StringUtils.isEmpty(getHValue(hssfRow.getCell(3)))){
                    		break;
                    	}
                    	
                        //读取列，从第一列开始  
                        SalaryDetail detail = new SalaryDetail();
                        detail.setEid(getHValue(hssfRow.getCell(0)));
                        detail.setUserName(getHValue(hssfRow.getCell(1)));
                        detail.setCardNo(getHValue(hssfRow.getCell(2)));
                        detail.setSalaries(Double.valueOf(getHValue(hssfRow.getCell(3))));
                        detail.setRemark(getHValue(hssfRow.getCell(4)));
                        detail.setSalaryDate(DateTimeUtil.date2Str(new Date(), DateTimeUtil.DATE_FORMAT_SHORTDAY));
                        details.add(detail);
                    }                     
                }  
            }  
            map.put("details", details);
            return map;  
        } catch (IOException e) {             
            e.printStackTrace();  
        } finally{  
            try {
            	if (input != null){
            		input.close();  
            	}
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
    
}