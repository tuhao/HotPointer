package com.apesRise.hotPointer.util;

import org.apache.log4j.Logger;

public final class LogHelper {
	
	static{
		org.apache.log4j.PropertyConfigurator.configure("conf/log4j.properties");
	}
	
	private static final Logger l = Logger.getLogger(LogHelper.class);

	public static void debug(Object o) {
		l.debug(o);
	}


	public static void info(Object o) {
		l.info(o);
	}

	public static void warn(Object o) {
		l.warn(o);
	}


	public static void error(Object o) {
		l.error(o);
	}


}