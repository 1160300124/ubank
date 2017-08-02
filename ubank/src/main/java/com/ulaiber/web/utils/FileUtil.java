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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.ulaiber.web.conmon.IConstants;

/**
 * 一组关于文件相关的方法集合
 * 
 * @author huangguoqing
 *
 */
public class FileUtil {


	/**
	 * 得到一个类的真实所在文件绝对路径<br/>
	 * 示例，在MAC下，getPath(SLFUtils.class)结果为：file:/Users/skywayman/Development/Java/zx_workspace/slfv5_common/target/test-classes/
	 * @param clazz 类文件
	 * @return 文件路径
	 */
	public static <T> String getPath(Class<T> clazz) {
		return clazz.getResource(File.separator).toString();
	}


	/**
	 * 根据给定的文件路径自动建立文件夹及子文件夹<br/>
	 * 示例，现有目录/Users/skywayman/temp，传入参数/Users/skywayman/temp/a/b/c/d将会依层级序次建立a，b，c，d四个目录，
	 * @param filepath 文件路径
	 * @return 是否建立成功
	 */
	public static boolean createDirectoryByPath(String filepath) {
		File dirFile = new File(filepath);// 创建文件夹名称
		boolean bFile = dirFile.exists();
		if (bFile) {
			return true;
		} else {
			bFile = dirFile.mkdirs();// 创建文件夹
			if (bFile == true) {
				return true;
			} else {
				return false;
			}
		}
	}

	/** 
	 * zip压缩功能. 压缩baseDir(文件夹目录)下所有文件，包括子目录，将压缩文件放到fileNameAndPath路径下<br/>
	 * 缺点：暂时不支持中文名压缩，换成APACHE中的ANT中的ZIP包即可
	 * @throws Exception 
	 */  
	public static boolean zipFile(String baseDir,String fileNameAndPath){  
		List<File> fileList=getSubFiles(new File(baseDir));
		InputStream is = null;
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(fileNameAndPath));
			ZipEntry ze = null;  
			byte[] buf = new byte[IConstants.FILE_OPERA_BUFFER];  
			int readLen = 0;  
			for(int i = 0; i <fileList.size(); i++) {  
				File f = (File)fileList.get(i);  
				ze = new ZipEntry(getAbsFileName(baseDir, f));  
				ze.setSize(f.length());  
				ze.setTime(f.lastModified());     
				zos.putNextEntry(ze);  
				is = new BufferedInputStream(new FileInputStream(f));  
				while ((readLen = is.read(buf, 0, IConstants.FILE_OPERA_BUFFER)) != -1) {  
					zos.write(buf, 0, readLen);  
				}  
				is.close();  
			}  
			zos.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}  finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(zos);
		}
		
		return true;
	}  

	/** 
	 * 给定根目录，返回另一个文件名的相对路径，用于zip文件中的路径. 
	 * @param baseDir java.lang.String 根目录 
	 * @param realFileName java.io.File 实际的文件名 
	 * @return 相对文件名 
	 */  
	private static String getAbsFileName(String baseDir, File realFileName){  
		File real = realFileName;  
		File base = new File(baseDir);  
		String ret = real.getName();  
		while (true) {  
			real = real.getParentFile();  
			if(real == null)   
				break;  
			if(real.equals(base))   
				break;  
			else  
				ret=real.getName() +"/" + ret;  
		}  
		return ret;  
	}  

	/** 
	 * 取得指定目录下的所有文件列表，包括子目录. 
	 * @param baseDir File 指定的目录 
	 * @return 包含java.io.File的List 
	 */  
	private static List<File> getSubFiles(File baseDir){  
		List<File> ret = new ArrayList<File>();  
		File[] tmp = baseDir.listFiles();  
		for (int i = 0; i < tmp.length; i++) {  
			if(tmp[i].isFile())  
				ret.add(tmp[i]);  
			if(tmp[i].isDirectory())  
				ret.addAll(getSubFiles(tmp[i]));  
		}  
		return ret;  
	}  

	/** 
	 * 解压缩功能. 
	 * 将zipfilename文件解压到outputPath目录下. 
	 * @throws Exception 
	 */  
	public static boolean upZipFile(String zipfilename,String outputPath) {  
		ZipFile zfile = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			zfile = new ZipFile(zipfilename);
			Enumeration<?> zList = zfile.entries();  
			ZipEntry ze = null;  
			byte[] buf = new byte[IConstants.FILE_OPERA_BUFFER];  
			while(zList.hasMoreElements()){  
				ze = (ZipEntry)zList.nextElement();         
				if(ze.isDirectory()){  
					File f = new File(outputPath+ze.getName());  
					f.mkdir();  
					continue;  
				}  
			    os = new BufferedOutputStream(new FileOutputStream(getRealFileName(outputPath, ze.getName())));  
				is = new BufferedInputStream(zfile.getInputStream(ze));  
				int readLen = 0;  
				while ((readLen = is.read(buf, 0, IConstants.FILE_OPERA_BUFFER)) != -1) {  
					os.write(buf, 0, readLen);  
				}  
				is.close();  
				os.close();   
			}  
			zfile.close();  
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}  finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
			IOUtils.closeQuietly(zfile);
		}
		return true;
	}  

	/** 
	 * 给定根目录，返回一个相对路径所对应的实际文件名. 
	 * @param baseDir 指定根目录 
	 * @param absFileName 相对路径名，来自于ZipEntry中的name 
	 * @return java.io.File 实际的文件 
	 */  
	private static File getRealFileName(String baseDir, String absFileName){  
		String[] dirs = absFileName.split(File.separator);  
		File ret = new File(baseDir);  
		if(dirs.length >= 1){  
			for (int i = 0; i < dirs.length - 1; i++) {  
				ret=new File(ret, dirs[i]);  
			}  
			if(!ret.exists())  
				ret.mkdirs();  
			ret = new File(ret, dirs[dirs.length-1]);  
			return ret;  
		} 
		return ret;  
	}


	/**
	 * 功能描述：拷贝一个目录或者文件到指定路径下，即把源文件拷贝到目标文件路径下
	 * @param source 源文件
	 * @param target 目标文件路径
	 * @return void
	 */
	public static boolean copy(File source, File target) {
		File tarpath = new File(target, source.getName());
		InputStream is = null;
		OutputStream os = null;
		try {
			if (source.isDirectory()) {
				tarpath.mkdir();
				File[] dir = source.listFiles();
				for (int i = 0; i < dir.length; i++) {
					copy(dir[i], tarpath);
				}
			} else {
				if (!tarpath.getParentFile().exists()){
					tarpath.getParentFile().mkdirs();
				}
				if (!tarpath.exists()){
					tarpath.createNewFile();
				}
				is = new FileInputStream(source); // 用于读取文件的原始字节流
				os = new FileOutputStream(tarpath); // 用于写入文件的原始字节的流
				byte[] buf = new byte[IConstants.FILE_OPERA_BUFFER];// 存储读取数据的缓冲区大小
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					os.write(buf, 0, len);
				}
				is.close();
				os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			IOUtils.closeQuietly(is);
			IOUtils.closeQuietly(os);
		}
		
		return true;
	}

	/**
	 * 删除文件
	 * @param path 路径
	 * @return
	 */
	public static boolean delFile(String path){
		File file = new File(path);
		boolean result = file.delete();
		return result;
	}
}
