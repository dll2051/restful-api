/**
 * 
 */
package com.tuotuogroup.service.impl;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tuotuogroup.dao.TaskDao;
import com.tuotuogroup.service.TaskService;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
	
	/**
	 * 日志工具
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 任务DAO
	 */
	@Resource(name="taskDao")
	private TaskDao taskDao;
}
