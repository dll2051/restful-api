package com.tuotuogroup.login.entity;

import javax.persistence.*;

/**
 * BaseEntity
 *
 * @author qlsc
 * @date 2015/8/10
 */
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",length = 20)
    private Long id;//主键ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
