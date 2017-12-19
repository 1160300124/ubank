package com.ulaiber.web.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import com.ulaiber.web.controller.backend.PermissionController;
import com.ulaiber.web.model.ExcelAO;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import net.sf.json.JSONException;
import org.springframework.web.multipart.MultipartFile;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档
 * Created by daiqingwen  on 2017/9/5.
 */
public class ExportExcel {
    //文件分隔符"\"（在 UNIX 系统中是“/”）
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

    private static Logger logger = Logger.getLogger(ExportExcel.class);

    /**
     *
     * @param jsonStr json字符串
     * @param sheaders 表格表头
     * @param fileName 文件名
     * @param title 表格标题名
     * @param response
     * @param request
     */
    public void export(String jsonStr, String sheaders, String fileName, String title,
                       HttpServletResponse response, HttpServletRequest request) throws IOException{
        try {
            // 将json字符串转换为json对象
            JSONArray jsonArray = new JSONArray(jsonStr);
            String[] headers= sheaders.substring(0,sheaders.length()-1).split(",");
            System.out.println(sheaders.substring(0,sheaders.length()-1));
            int iSize = jsonArray.length();
            List<List<String>> list = new ArrayList<List<String>>();
            for (int i = 0; i < iSize; i++) {
                List<String> line = new ArrayList<String>();
                JSONObject jsonObject =  jsonArray.getJSONObject(i);
                System.out.println(jsonObject.toString()+"-----");
                Iterator iterator = jsonObject.keys();
                String value = null;
                int j=0;
                while (iterator.hasNext()) {
                    iterator.next();
                    value = jsonObject.getString(headers[j]);
                    //表格内容
                    line.add(value);
                    j++;
                    System.out.println(value);
                }
                list.add(line);
            }

            for(List<String> line:list){
                for(String s:line){
                    System.out.print(s+"\t");
                }
                System.out.println();
            }

            String docsPath = request.getSession().getServletContext().getRealPath("exportExcel");
            File file = new File(docsPath);
            if(!file.exists()){
                file.mkdirs();
            }
            //文件路径
            String filePath = docsPath + FILE_SEPARATOR + fileName;
            System.out.println(filePath);

            OutputStream out = new FileOutputStream(filePath);
            exportExcel(title,headers, list, out);  //导出excel
            out.close();
            JOptionPane.showMessageDialog(null, "导出成功!");
            System.out.println(">>>>>>>>>>>>>excel导出成功！");

            //下载
            download(filePath, response);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     * @param title 表格标题名
     * @param headers 表格属性列名数组
     * @param list 需要显示的数据集合
     * @param out 与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public void exportExcel(String title, String[] headers,List<List<String>> list, OutputStream out){
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("leno");
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }
        // 遍历集合数据，产生数据行
        for(int i=0;i<list.size();i++){
            List<String> line = list.get(i);
            row = sheet.createRow(i+1);
            for(int j=0;j<line.size();j++){
                HSSFCell cell = row.createCell(j);
                cell.setCellStyle(style2);
                    String value = line.get(j);
                    try {
                        // 判断值的类型后进行强制类型转换
                        String textValue = null;
                        // 其它数据类型都当作字符串简单处理
                        textValue = value.toString();
                        // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                        if (textValue != null) {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } finally {
                        // 清理资源
                    }
            }

        }

        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 下载
     * @param path 文件名
     * @param response
     */
    public void download(String path, HttpServletResponse response){
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="+ new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  去读Excel的方法readExcel，该方法的入口参数为一个File对象
     * @param file
     * @throws IOException
     */
    public Map<String,Object> readExcel(MultipartFile file) throws IOException {
        logger.info("开始导入excel");
        Map<String,Object> resultMap = new HashMap<>();
        try {
            String path = file.getOriginalFilename();
            path = path.substring(path.lastIndexOf("."),path.length());
            List<ExcelAO> TList = new ArrayList<>();
            List<ExcelAO> FList = new ArrayList<>();
            if(".xlsx".equals(path)){
                //InputStream stream = new FileInputStream(file);
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

                int rowstart = xssfSheet.getFirstRowNum();
                int rowEnd = xssfSheet.getLastRowNum();
                logger.info("excel总记录数为：" + (rowEnd-1));
                //将总记录数返回给前台
                resultMap.put("totalCount",rowEnd - 1);
                //获取所有行
                for(int i=rowstart;i<=rowEnd;i++) {
                    logger.info("开始导入第（"+(i+1) +"）条数据");
                    //从第二行开始
                    XSSFRow row = xssfSheet.getRow(i + 1);
                    if(null == row) continue;
                    int cellStart = row.getFirstCellNum();
                    int cellEnd = row.getLastCellNum();
                    ExcelAO excelAO =  new ExcelAO();
                    StringBuffer sb = new StringBuffer();
                    //遍历每一行中的表格数据
                    for(int k = cellStart ; k <= cellEnd ;k++) {
                        XSSFCell cell = row.getCell(k);
                        if(k == 0){ //判断序号
                            excelAO.setId(cell.toString());
                            continue;
                        }else if(k == 1){ //判断姓名
                            if(StringUtil.isEmpty(cell.toString())){
                                sb.append("名字不能为空,");
                            }
                            excelAO.setName(cell.toString());
                            continue;
                        }else if(k == 2){ //判断身份证
                            String idcard = cell.toString();
                            if (StringUtil.isEmpty(cell.toString())){
                                sb.append("身份证不能为空,");
                            }else if(idcard.length() != 18){
                                sb.append("身份证长度为18位,");
                            }
                            excelAO.setIDCard(idcard);
                            continue;
                        }else if(k == 3){ //判断电话号码
                            String mobile = cell.toString();
                            if (StringUtil.isEmpty(cell.toString())){
                                sb.append("电话号码不能为空,");
                            }else if(mobile.length() != 11){
                                sb.append("电话号码长度为11位,");
                            }
                            excelAO.setMobile(mobile);
                            continue;
                        }else if(k == 4){ //判断入职时间
                            if(StringUtil.isEmpty(cell.toString())){
                                sb.append("入职时间不能为空,");
                            }
                            excelAO.setEntryTime(cell.toString());
                            continue;
                        }else if(k == 5){ //薪资
                            if(StringUtil.isEmpty(cell)){
                                sb.append("工资不能为空,");
                            }
                            excelAO.setSalary(Double.parseDouble(cell.toString()));
                            continue;
                        }else if(k == 6){ //部门
                            if(StringUtil.isEmpty(cell)){
                                sb.append("部门不能为空,");
                            }
                            excelAO.setDept(cell.toString());
                            continue;
                        }else{
                            if(!"".equals(sb.toString())){
                                String str = sb.substring(0,sb.length()-1);
                                excelAO.setMessage(str);
                                FList.add(excelAO);
                            }else{
                                TList.add(excelAO);
                            }

                            break;
                        }

                    }
                }
                logger.info("导出完毕");
                resultMap.put("true",TList);
                resultMap.put("false",FList);
            }else if(".xls".equals(path)){
                logger.info("开始导入excel");
                POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file.getInputStream());
                HSSFWorkbook hssfWorkbook =  new HSSFWorkbook(poifsFileSystem);
                HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

                int rowstart = hssfSheet.getFirstRowNum();
                int rowEnd = hssfSheet.getLastRowNum();
                logger.info("excel总记录数为：" + (rowEnd-1));
                //将总记录数返回给前台
                resultMap.put("totalCount",rowEnd - 1);
                for(int i=rowstart;i<=rowEnd;i++) {
                    logger.info("开始导入第（"+(i+1) +"）条数据");
                    HSSFRow row = hssfSheet.getRow(i + 1);
                    if(null == row) continue;
                    int cellStart = row.getFirstCellNum();
                    int cellEnd = row.getLastCellNum();
                    ExcelAO excelAO =  new ExcelAO();
                    StringBuffer sb = new StringBuffer();

                    for(int k=cellStart;k<=cellEnd;k++){
                        HSSFCell cell = row.getCell(k);
                        if(k == 0){ //判断序号
                            excelAO.setId(cell.toString());
                            continue;
                        }else if(k == 1){ //判断姓名
                            if(StringUtil.isEmpty(cell.toString())){
                                sb.append("名字不能为空,");
                            }
                            excelAO.setName(cell.toString());
                            continue;
                        }else if(k == 2){ //判断身份证
                            String idcard = cell.toString();
                            if (StringUtil.isEmpty(cell.toString())){
                                sb.append("身份证不能为空,");
                            }else if(idcard.length() != 18){
                                sb.append("身份证长度为18位,");
                            }
                            excelAO.setIDCard(idcard);
                            continue;
                        }else if(k == 3){ //判断电话号码
                            String mobile = cell.toString();
                            if (StringUtil.isEmpty(cell.toString())){
                                sb.append("电话号码不能为空,");
                            }else if(mobile.length() != 11){
                                sb.append("电话号码长度为11位,");
                            }
                            excelAO.setMobile(mobile);
                            continue;
                        }else if(k == 4){ //判断入职时间
                            if(StringUtil.isEmpty(cell.toString())){
                                sb.append("入职时间不能为空,");
                            }
                            excelAO.setEntryTime(cell.toString());
                            continue;
                        }else if(k == 5){ //薪资
                            if(StringUtil.isEmpty(cell)){
                                sb.append("工资不能为空,");
                            }
                            excelAO.setSalary(Double.parseDouble(cell.toString()));
                            continue;
                        }else if(k == 6){ //部门
                            if(StringUtil.isEmpty(cell)){
                                sb.append("部门不能为空,");
                            }
                            excelAO.setDept(cell.toString());
                            continue;
                        }else{
                            if(!"".equals(sb.toString())){
                                String str = sb.substring(0,sb.length()-1);
                                excelAO.setMessage(str);
                                FList.add(excelAO);
                            }else{
                                TList.add(excelAO);
                            }
                            break;
                        }

                    }
                }
                logger.info("导出完毕");
                resultMap.put("true",TList);
                resultMap.put("false",FList);
            }
        }catch (Exception e){
            logger.error("导入excel失败" ,e);
        }

        return resultMap;

    }

}
