package com.imooc.seckill.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imooc.seckill.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author : WangPingChun
 * 2018-07-30
 */
@Slf4j
public class UserUtils {
	private static void createUser(int count) throws Exception {
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			User user = new User();
			user.setId(13000000000L + i);
			user.setLoginCount(1);
			user.setNickname("user" + i);
			user.setRegisterDate(new Date());
			user.setSalt("1a2b3c4d");
			user.setPassword(MD5Utils.dbPassword("123456", user.getSalt()));
			userList.add(user);
		}
		log.info("--- create user ---");

		// 插入数据库

//		Connection conn = DBUtils.getConn();
//		String sql = "insert into seckill_user(login_count, nickname, register_date, salt, password, id)values(?,?,?,?,?,?)";
//		PreparedStatement pstmt = conn.prepareStatement(sql);
//		for (User user : userList) {
//			pstmt.setInt(1, user.getLoginCount());
//			pstmt.setString(2, user.getNickname());
//			pstmt.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
//			pstmt.setString(4, user.getSalt());
//			pstmt.setString(5, user.getPassword());
//			pstmt.setLong(6, user.getId());
//			pstmt.addBatch();
//		}
//		pstmt.executeBatch();
//		pstmt.close();
//		conn.close();


		String loginUrl = "http://localhost:8080/login";
		File file = new File("D:/token.txt");
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();

		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.seek(0);
		for (User user : userList) {
			URL url = new URL(loginUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStream out = connection.getOutputStream();
			String params = "mobile=" + user.getId() + "&password=" + MD5Utils.md5Password("123456");
			out.write(params.getBytes());
			out.flush();
			InputStream inputStream = connection.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte buff[] = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(buff)) >= 0) {
				bout.write(buff, 0, len);
			}
			inputStream.close();
			bout.close();
			String response = new String(bout.toByteArray());
			JSONObject jo = JSON.parseObject(response);
			String token = jo.getString("data");
			System.out.println("create token : " + user.getId());

			String row = user.getId() + "," + token;
			raf.seek(raf.length());
			raf.write(row.getBytes());
			raf.write("\r\n".getBytes());
			System.out.println("write to file : " + user.getId());
		}
		raf.close();

		System.out.println("over");
	}

	public static void main(String[] args) throws Exception {
		createUser(5000);
	}
}
