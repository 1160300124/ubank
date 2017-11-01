package com.ulaiber.web.model;

/**
 * 封装结果对象，可用于前后台交互
 * @author huangguoqing
 *
 */
public class ResultInfo {

	//请求返回码
	private int code;

	//返回信息
	private String message;

	//返回数据
	private Object data;

	public ResultInfo(){

	}

	public ResultInfo(int code, String message){
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}


}
