package com.imooc.seckill.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 *
 * @author : WangPingChun
 * 2018-07-30
 */
public class DBUtils {
	private static Properties props;

	static {
		try {
			InputStream is = DBUtils.class.getClassLoader().getResourceAsStream("application.yml");
			props = new Properties();
			props.load(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConn() throws Exception {
		String url = props.getProperty("url");
		String username = props.getProperty("username");
		String password = "WANGpc@0303";
		String driver = props.getProperty("driver-class-name");
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}
}
