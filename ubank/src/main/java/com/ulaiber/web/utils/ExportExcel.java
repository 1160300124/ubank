package com.ulaiber.web.utils;


import net.sf.json.JSONException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 利用开源组件POI3.0.2动态导出EXCEL文档
 * Created by daiqingwen  on 2017/9/5.
 */
public class ExportExcel {
    //文件分隔符"\"（在 UNIX 系统中是“/”）
    public static final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");

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
                       HttpServletResponse response, HttpServletRequest request) throws IOException,FileNotFoundException{
        try {
            // 将json字符串转换为json对象
            JSONArray jsonArray = new JSONArray(jsonStr);
            String[] headers= sheaders.substring(0,sheaders.length()-1).split(",");
            System.out.println(sheaders.substring(0,sheaders.length()-1));
            int iSize = jsonArray.length();
            List<List> list = new ArrayList<List>();
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
    private void exportExcel(String title, String[] headers,List<List> list, OutputStream out){
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
        for (short i = 0; i < headers.length; i++) {
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
    private void download(String path, HttpServletResponse response){
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
}
