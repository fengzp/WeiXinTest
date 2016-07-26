package com.feng.web.model;

public class CreateQrCodeResponse {

	private String ticket;
	private Integer expire_seconds;
	private String url;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Integer getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "CreateQrCodeResponse [ticket=" + ticket + ", expire_seconds="
				+ expire_seconds + ", url=" + url + "]";
	}
	
}
