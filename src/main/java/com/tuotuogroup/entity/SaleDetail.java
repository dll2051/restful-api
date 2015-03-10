package com.tuotuogroup.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_saledetail")
public class SaleDetail implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/**
 * "id": "1150766", 
            "title": "华硕（ASUS） Z97-A 主板 （Intel Z97/LGA 1150） 849元（赠 玄冰400）", 
            "img": "http://7bv7rb.com1.z0.glb.clouddn.com/296db4e5f0920740399df91524af26ba.jpg", 
            "url": "http://zhufu.sinaapp.com/api/go.php?id=1150766", 
            "source": "SMZDM发现", 
            "mallname": "京东商城", 
            "time": "4分钟", 
            "detail": "华硕旗舰Z97芯片组上也有价廉物美的型号——京东特供版。Z97-A是上一代Z87-A这款经典款的升级型号。主板提供8相供电，支持DDR3 3200（超频），同时添加了高品质的音频区域：Crystal Sound 2(美声大师)以及Intel I218-V网卡芯片搭配的Turbo LAN网络优化软件。虽然是特供版但是基础的东西还是很齐全。 京东报价9			..."
 */
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 20)
	@GeneratedValue(generator = "saleGenerator")
	@GenericGenerator(name = "saleGenerator", strategy = "com.tuotuogroup.utils.UUIDGenerator")
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
	@Column(name = "IMG", length = 50)
	private String img;
	@Column(name = "URL", length = 50)
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