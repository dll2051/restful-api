package com.tuotuogroup.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TbAuthRole
 *
 * @author qlsc
 * @date 2015/8/11
 */
@Entity
@Table(name = "tb_auth_role")
public class TbAuthRole extends BaseEntity{
    @Column(name = "role",length = 100)
    private String role; //角色标识 程序中判断使用,如"admin"
    @Column(name = "description",length = 100)
    private String description; //角色描述,UI界面显示使用
    private Boolean available = Boolean.FALSE; //是否可用,如果不可用将不会添加给用户
}
