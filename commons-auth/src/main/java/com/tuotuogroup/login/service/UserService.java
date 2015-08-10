package com.tuotuogroup.login.service;

import com.tuotuogroup.login.entity.TbAuthUser;

import java.util.Set;

/**
 * UserService
 *
 * @author qlsc
 * @date 2015/8/10
 */
public interface UserService {
    TbAuthUser findUserByLoginName(String username);

    Set<String> findRoles(String username);

    Set<String> findPermissions(String username);
}
