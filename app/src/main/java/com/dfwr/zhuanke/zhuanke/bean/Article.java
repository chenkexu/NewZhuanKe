package com.dfwr.zhuanke.zhuanke.bean;

import java.io.Serializable;
import java.util.Date;

public class Article implements Serializable{


	private static final long serialVersionUID = 7619167491040543604L;
	private Integer aid;
	private String time;
	private String content;
	private int click;
	private String type;
	private String title;
	private String headImg;
	private String articleLink;
	private long createTime;


	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getArticleLink() {
		return articleLink == null ? "" : articleLink;
	}

	public void setArticleLink(String articleLink) {
		this.articleLink = articleLink;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}


	public String getTime() {
		return time == null ? "" : time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content == null ? "" : content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getClick() {
		return click;
	}

	public void setClick(int click) {
		this.click = click;
	}

	public String getType() {
		return type == null ? "" : type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title == null ? "" : title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeadImg() {
		return headImg == null ? "" : headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Article() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Article(Integer aid, Date createTime, String time, String content, int click, String type, String title,
			String headImg) {
		super();
		this.aid = aid;
		this.time = time;
		this.content = content;
		this.click = click;
		this.type = type;
		this.title = title;
		this.headImg = headImg;
	}
	
}
