package com.tuotuogroup.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_saledetail")
public class SaleDetail implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 20)
//	@GeneratedValue(generator = "saleGenerator")
//	@GenericGenerator(name = "saleGenerator", strategy = "com.tuotuogroup.utils.UUIDGenerator")
	private String id;
	@Column(name = "TITLE", length = 200)
	private String title;
	@Column(name = "DETAIL", length = 500)
	private String detail;
	@Column(name = "STATUS", length = 1)
	private Integer status;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length = 7)
	private Date createTime;
	@Column(name = "IMG", length = 100)
	private String img;
	@Column(name = "URL", length = 100)
	private String url;
	@Column(name = "SOURCE", length = 50)
	private String source;
	@Column(name = "MALLNAME", length = 50)
	private String mallname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMallname() {
		return mallname;
	}
	public void setMallname(String mallname) {
		this.mallname = mallname;
	}
	
}