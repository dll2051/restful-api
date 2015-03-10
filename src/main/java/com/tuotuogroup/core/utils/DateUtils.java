package com.tuotuogroup.core.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	/***************************************************************************
	 * The number of milliseconds in a minute.
	 */
	private final static long MS_IN_MINUTE = 60 * 1000;

	/***************************************************************************
	 * The number of milliseconds in an hour.
	 */
	private final static long MS_IN_HOUR = 60 * 60 * 1000;

	/***************************************************************************
	 * The number of milliseconds in a day.
	 */
	// private final static long MS_IN_DAY = 24 * 60 * 60 * 1000;
	/**
	 * 本地日期格式符号表
	 */
	public final static DateFormatSymbols localDateFormatSymbols = new DateFormatSymbols(Locale.getDefault());

	public DateUtils() {
	}

	public static String Format(String s, Date date) {
		if (date == null) {
			return null;
		}
		if (s == null) {
			return FormatDateTime(date);
		}
		try {
			return (new SimpleDateFormat(s)).format(date);
		} catch (Exception _ex) {
			return FormatDateTime(date);
		}
	}

	public static String FormatDate(Date date) {
		if (date == null) {
			return null;
		} else {
			return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
		}
	}

	public static String FormatDateDatabase(Date date) {
		if (date == null) {
			return null;
		} else {
			return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
		}
	}

	public static String FormatDateTimeDatabase(Date date) {
		if (date == null) {
			return null;
		} else {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
		}
	}

	public static String FormatDateTime(Date date) {
		if (date == null) {
			return null;
		} else {
			return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
		}
	}

	public static String FormatTime(Date date) {
		if (date == null) {
			return null;
		} else {
			return (new SimpleDateFormat("HH:mm:ss")).format(date);
		}
	}

	public static String FormatDateTime(long s) {
		Date date = new Date(s);
		String _date = FormatDateTimeDatabase(date);
		return _date;
	}

	protected static Date _parseDate(String s, String s1) {
		if (s == null || s1 == null) {
			return null;
		} else {
			SimpleDateFormat simpledateformat = new SimpleDateFormat(s1);
			ParsePosition parseposition = new ParsePosition(0);
			String s2 = s.replace('/', '-');
			return simpledateformat.parse(s2, parseposition);
		}
	}

	public static Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public static String getCurrentDateForDatabase() {
		return (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime());
	}

	public String getCurrentDateTime() {
		String s = "yyyy年MM月dd日 HH:mm:ss";
		return (new SimpleDateFormat(s)).format(Calendar.getInstance().getTime());
	}

	public static Date getDate(String s) {
		return _parseDate(s, "yyyy-MM-dd");
	}

	public static Date getDateTime(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		return _parseDate(s, "yyyy-MM-dd HH:mm:ss");
	}

	public static int[] getFormatDate(Date date) {
		int ai[] = new int[3];
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy");
		String s = simpledateformat.format(date);
		ai[0] = Integer.parseInt(s);
		SimpleDateFormat simpledateformat1 = new SimpleDateFormat("MM");
		String s1 = simpledateformat1.format(date);
		ai[1] = Integer.parseInt(s1);
		SimpleDateFormat simpledateformat2 = new SimpleDateFormat("dd");
		String s2 = simpledateformat2.format(date);
		ai[2] = Integer.parseInt(s2);
		return ai;
	}

	public static Date getRequestDate(HttpServletRequest httpservletrequest, String s) {
		if (httpservletrequest == null || s == null) {
			throw new IllegalArgumentException();
		}
		String s1 = httpservletrequest.getParameter(s);
		if (s1 == null) {
			return null;
		} else {
			return getDate(s1);
		}
	}

	public static Date getTime(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}
		StringTokenizer stringtokenizer = new StringTokenizer(s, " :-/.");
		int i = 0;
		int ai[] = new int[3];
		while (stringtokenizer.hasMoreTokens()) {
			if (i > 2) {
				break;
			}
			try {
				ai[i++] = Integer.parseInt(stringtokenizer.nextToken());
			} catch (NumberFormatException _ex) {
				return null;
			}
		}
		Calendar calendar = Calendar.getInstance();
		if (i == 1) {
			calendar.set(11, ai[0]);
			calendar.set(12, 0);
			calendar.set(13, 0);
			return calendar.getTime();
		}
		if (i == 2) {
			calendar.set(11, ai[0]);
			calendar.set(12, ai[1]);
			calendar.set(13, 0);
			return calendar.getTime();
		}
		if (i == 3) {
			calendar.set(11, ai[0]);
			calendar.set(12, ai[1]);
			calendar.set(13, ai[2]);
			return calendar.getTime();
		} else {
			return null;
		}
	}

	public static Date getTomorrow(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(calendar.MILLISECOND, 0);
			calendar.add(5, 1);
			return calendar.getTime();
		}
	}

	public static Date getYesterday(Date date) {
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(calendar.MILLISECOND, 0);
			calendar.add(5, -1);
			return calendar.getTime();
		}
	}

	public String getRssDateTime(String sDate) {
		Date date = DateParser.parseDate(sDate);
		if (date == null) {
			try {
				date = new Date();
				date.parse(sDate);
				return date.toLocaleString();
			} catch (Exception e) {
				return sDate;
			}

		} else
			return Format("yy/MM/dd HH:mm:ss", date);
	}

	public static int getDay(Date date) {
		if (date == null)
			return 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * Get the date of the first day of the month in which this date resides.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.getMinimum(Calendar.DAY_OF_MONTH));

		return calendar.getTime();
	}

	/**
	 * Get the date of the last day of the month in which this date resides.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);

		return calendar.getTime();
	}

	public static Date getPrevMonth(Date date) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);

		return calendar.getTime();
	}

	public static String isoFormat(Date inputDate) {

		// Setup the date format and convert the given date.
		SimpleDateFormat isoFormat_ = new SimpleDateFormat(ISO_FORMAT);
		String dateString = isoFormat_.format(inputDate);
		// Determine the time zone and concatenate the time zone
		// designator
		// onto the formatted date/time string.
		TimeZone tz = isoFormat_.getTimeZone();
		String tzName = tz.getDisplayName();
		if (tzName.equals("Greenwich Mean Time")) {
			dateString = dateString.concat("Z");
		} else {
			// Determine the hour offset. Add an hour if daylight
			// savings
			// is in effect.
			long tzOffsetMS = tz.getRawOffset();
			long tzOffsetHH = tzOffsetMS / MS_IN_HOUR;
			if (tz.inDaylightTime(inputDate)) {
				tzOffsetHH = tzOffsetHH + 1;
			}
			String hourString = String.valueOf(Math.abs(tzOffsetHH));
			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}

			// Determine the minute offset.
			long tzOffsetMMMS = tzOffsetMS % MS_IN_HOUR;
			long tzOffsetMM = 0;
			if (tzOffsetMMMS != 0) {
				tzOffsetMM = tzOffsetMMMS / MS_IN_MINUTE;
			}
			String minuteString = String.valueOf(tzOffsetMM);
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}

			// Determine the sign of the offset.
			String sign = "+";
			if (String.valueOf(tzOffsetMS).indexOf("+") != -1) {
				sign = "-";
			}

			dateString = dateString.concat(sign + hourString + ":" + minuteString);
		}

		return (dateString);
	}

	/***************************************************************************
	 * Parse the given date/time string in ISO 8601 format and return the
	 * resulting <code>Date</code> object. The format is as follows:
	 * "yyyy-MM-dd'T'HH:mm:ss.SSS[Z|[+|-]HH:mm]".
	 * 
	 * @param inputString
	 *            The string to be parsed.
	 * @return The resulting Date object.
	 * @throws ParseException
	 *             If the string does not match the date/time format.
	 */
	public static Date isoParse(String inputString) throws ParseException {
		if (StringUtils.isBlank(inputString))
			return null;
		// Setup the date format.
		SimpleDateFormat isoFormat_ = new SimpleDateFormat(ISO_FORMAT);
		isoFormat_.setLenient(false);

		// The length of the input string should be at least 24
		// characters.
		if (inputString.length() < 20) {
			throw new ParseException(
					"An exception occurred because the input date/time string was not at least 20 characters in length.",
					inputString.length());
		}

		// Evaluate the the specified offset and set the time zone.
		String offsetString = inputString.substring(19);
		if (offsetString.equals("Z")) {
			isoFormat_.setTimeZone(TimeZone.getTimeZone("Greenwich Mean Time"));
		} else if (offsetString.startsWith("-") || offsetString.startsWith("+")) {
			SimpleDateFormat offsetFormat = new SimpleDateFormat();
			if (offsetString.length() == 3) {
				offsetFormat.applyPattern("HH");
			} else if (offsetString.length() == 6) {
				offsetFormat.applyPattern("HH:mm");
			} else {
				throw new ParseException(
						"An exception occurred because the offset portion was not the valid length of 3 or 6 characters.",
						25);
			}

			// Validate the given offset.
			offsetFormat.setLenient(false);
			// Date offsetDate = offsetFormat.parse(offsetString.substring(1));

			// Set the time zone with the validated offset.
			isoFormat_.setTimeZone(TimeZone.getTimeZone("GMT" + offsetString));
		} else {
			throw new ParseException(
					"An exception occurred because the offset portion of the input date/time string was not 'Z' or did not start with '+' or '-'.",
					24);
		}

		// Parse the given string.
		Date parseDate = isoFormat_.parse(inputString);

		return (parseDate);

	}

	public static void main(String args[]) throws ParseException {
		System.out.println(FormatDate(isoParse("2011-10-01T23:56:59")));
	}
}
