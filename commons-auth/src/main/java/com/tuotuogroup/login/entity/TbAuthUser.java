package com.tuotuogroup.login.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * TbAuthUser
 *
 * @author qlsc
 * @date 2015/8/10
 */
@Entity
@Table(name = "tb_auth_user")
public class TbAuthUser extends BaseEntity {
    @Column(name = "organization_id",length = 20)
    private Long organizationId; //所属公司
    @Column(name = "username",length = 100)
    private String username; //用户名
    @Column(name = "password",length = 100)
    private String password; //密码
    @Column(name = "salt",length = 100)
    private String salt; //加密密码的盐

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
