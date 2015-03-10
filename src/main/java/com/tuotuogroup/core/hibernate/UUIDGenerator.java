package com.tuotuogroup.core.hibernate;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.type.Type;

public class UUIDGenerator  implements IdentifierGenerator, Configurable {

	@Override
	public void configure(Type type, Properties params, Dialect d)
			throws MappingException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 * @param session
	 * @param object
	 */
	@Override
	public Serializable generate(SessionImplementor session, Object object)
			throws HibernateException {
		return UUID.randomUUID().toString();
	}

}
