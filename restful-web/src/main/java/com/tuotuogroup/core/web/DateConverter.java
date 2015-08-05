package com.tuotuogroup.core.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.tuotuogroup.core.utils.DateUtils;

public class DateConverter implements Converter<String, Date> {
	public static final DateFormat DF_LONG = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final DateFormat DF_SHORT = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 短类型日期长度
	 */
	public static final int SHORT_DATE = 10;

	@Override
	public Date convert(String text) {
		text = text.trim();
		if (text.length() <= SHORT_DATE) {
			return DateUtils.getDate(text);
		} else {
			return DateUtils.getDateTime(text);
		}
	}
}
