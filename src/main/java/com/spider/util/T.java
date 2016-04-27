package com.spider.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
 

public class T {
	public T() {
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(List list){
		return (list == null || list.size() ==0);
	}
	
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static String format(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/** ************************取默认value值的********************************* */
	public static String stringValue(String v, String def) {
		if (v == null || v.length() == 0)
			return def;
		return v.trim();
	}

	public static String[] stringArrayValue(String[] v, String[] def) {
		if (v == null || v.length == 0)
			return def;
		return v;
	}

	public static byte byteValue(String v, byte def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Byte.parseByte(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static char charValue(String v, char def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return (char) Integer.parseInt(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static int intValue(String v, int def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Integer.parseInt(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static Integer integerValue(String v) {
		return integerValue(v, null);
	}
	
	public static Integer integerValue(String v, int def) {
		if (isBlank(v))
			return new Integer(def);
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return new Integer(def);
		}
	}
	public static Integer integerValue(String v, Integer def) {
		if (isBlank(v))
			return def;
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return def;
		}
	}


	public static long longValue(String v, long def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Long.parseLong(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static boolean booleanValue(String v, boolean def) {
		if (v == null || v.length() == 0)
			return def;

		if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes")
				|| v.equalsIgnoreCase("1")) {
			return true;
		} else if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("no")
				|| v.equalsIgnoreCase("0")) {
			return false;
		} else {
			return def;
		}
	}

	public static float floatValue(String v, float def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Float.parseFloat(v.trim());
		} catch (Exception e) {
			return def;
		}
	}
	
	public static float floatValue(String v ,int remain, float def) {
		try {
			BigDecimal bd = new BigDecimal(v);
			bd = bd.setScale(remain,BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		} catch (Exception e) {
			return def;
		}
	}

	public static double doubleValue(String v, double def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Double.parseDouble(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static Date dateValue(String v, String fm, Date def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return new SimpleDateFormat(fm).parse(v.trim());
		} catch (Exception e) {
			return def;
		}
	}
	
	public static Date getNow() {
	    return new Date();
	}

	public static Date dateValue(String v, Date def) {
		return dateValue(v, "yyyy-MM-dd", def);
	}
	
	//自动判断时间格式
	//timeStart=true,精确到时间的，timeStart=false精确到时间的结束，用于没有指定时分秒的情
	public static Date dateValue2(String v, Date def, boolean timeStart) {
		if (v == null || v.length() == 0)return def;
		if(v.matches("\\d+-\\d+-\\d+ \\d+:\\d+")){
			if(timeStart){
				v += ":00";
			}else{
				v += ":59";
			}
		}else if(v.matches("\\d+-\\d+-\\d+ \\d+")){
			if(timeStart){
				v += ":00:00";
			}else{
				v += ":59:59";
			}
		}else if(v.matches("\\d+-\\d+-\\d+")){
			if(timeStart){
				v += " 00:00:00";
			}else{
				v += " 23:59:59";
			}
		}
		return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
	}

	public static Date datetimeValue(String v, Date def) {
		return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
	}	
    
    public static Date getAfterNow(int seconds) {
		return new Date(System.currentTimeMillis() + seconds * 1000);
	} 

	// 得到今天凌晨的时
	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	//得到今日23 时间
	public static Date getTodayLast() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

	//n天前或后 + -
    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);

        return new Date(cal.getTime().getTime());
    }

	//n月前或后 + -
    public static Date addMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);

        return new Date(cal.getTime().getTime());
    }

	//n小时前或后 + -
    public static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);

        return new Date(cal.getTime().getTime());
    }

	//n小时前或后 + -
    public static Date addMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);

        return new Date(cal.getTime().getTime());
    }
    
	public static Date getTheDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	//得到今日23时间
	public static Date getLastTheDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
	
	// 本月的开
    public static Date getMonthStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
        return getTheDay(cal.getTime());
    }
    
    // 本月的结
    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH,1); 
        cal.set(Calendar.DAY_OF_MONTH,1); 
        cal.add(Calendar.DAY_OF_MONTH,-1); 
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
        return getTheDay(cal.getTime());
    }
    
	/**
	 * 周开始日时间)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekStart(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return getTheDay(cal.getTime());
	}

	/**
	 * 周结束时间)
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, 7);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return getTheDay(cal.getTime());
	}
	
	// 得到这个星期期的开始从getFirstDayOfWeek()得到
	public static Date getThisWeekStart() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
		return getTheDay(cal.getTime());
	}
	
	//得到上一个星期的
	public static Date getLastWeekStart(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
		cal.add(Calendar.DATE, -7);
		return getTheDay(cal.getTime());
	}
	
	//得到上一个星期的结束
	public static Date getLastWeekEnd(){
		return addDay(getThisWeekStart(), -1);
	}    
    
    
	public static String getRoot(HttpServletRequest request){ 
		String root = "http://" + request.getHeader("host") + request.getContextPath();
		return root;
	}
	
	public static String getBaseUrl(HttpServletRequest request){
		String root = getRoot(request); 
        String base = (request.getContextPath()!=null&&request.getRequestURI()!=null?request.getRequestURI().replaceFirst(request.getContextPath(), ""):request.getRequestURI());
		if(base != null && !"".equals(base)){
			while(base.startsWith("/")){
				base = base.replaceFirst("/", "");
			}
		} 
        String baseUrl = root + base;
		return baseUrl;
	}
	
	public static boolean isIE(HttpServletRequest request){
    	return request.getHeader( "USER-AGENT" ).toLowerCase().indexOf( "msie" ) >  0?true:false;
    }
	
	//计算短信条数
	public static int getLength(String str) {
	    int count = 1;
	    int length = str.length();
	    if (length > 70) {
	        int idxc = length % 67;
	        count = length / 67;
	        if (idxc > 0) {
	            count ++;
	        }	        
	    }
	    return count;
	}
	
	public static String getJsonString(int statusCode, String message) {
		JSONObject json = new JSONObject();
        json.put("statusCode", statusCode);
        json.put("message", message);
        return json.toJSONString();
	}
	
	
}

