/**
 * 
 */
package com.wondersgroup.entity;

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
@Table(name = "tb_sale_info")
public class SaleInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5735209924679651071L;
	/*
    "id": "1137581",
    "title": "AOC U2870VQE/WW 28英寸4K UHD显示器 1899元",
    "img": "http://7bv7rb.com1.z0.glb.clouddn.com/731c00f5671abd9528d1123382b41d90.jpg",
    "url": "http://zhufu.sinaapp.com/api/go.php?id=1137581",
    "source": "SMZDM发现",
    "mallname": "京东商城",
    "time": "4分钟",
    "detail": "4K是早已进入消费级别的领域，很多朋友也已经尝鲜，说明4K已经有了进入普及，至少是有相对应专业应用需求的用户圈子中的普及趋势。所以4K显示器在市场中表现出来的爆发力是其他非显示器4K领域所难以比拟的。 AOC这款4K显示器作为其进军4K领域的第二款产品，同时也是其旗下4K显示器爆款产品，自\t\t\t..."
	*/
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "saleinfoGenerator")
	@GenericGenerator(name = "saleinfoGenerator", strategy = "com.wondersgroup.core.hibernate.UUIDGenerator")
	private String id;
	@Column(name = "title", length = 100, nullable = false)
	private String title;
	@Column(name = "img", length = 100, nullable = false)
	private String img;
	@Column(name = "url", length = 100, nullable = false)
	private String url;
	@Column(name = "source", length = 50, nullable = false)
	private String source;
	@Column(name = "mallname", length = 50, nullable = false)
	private String mallname;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time",nullable = false)
	public Date time;
	/**
	 * 备注
	 * <P>
	 * <li>允许长度200</li>
	 */
	@Column(name = "detail", length = 1000, nullable = false)
	private String detail;
	/**
	 * 是否缓存
	 * <P>
	 * <li>1:缓存</li>
	 * <li>0:不缓存</li>
	 */
	@Column(name = "is_cache", length=1)
	private Integer isCache;
	@Column(name="is_visabled", length=1)
	private Integer isVisabled;
	
}
