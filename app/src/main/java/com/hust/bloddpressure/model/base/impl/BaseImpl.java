/**
 * Copyright(C) 2020 Hust
 * BaseDaoImpl.java Oct 24, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.base.impl;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hust.bloddpressure.model.base.BaseInter;
import com.hust.bloddpressure.util.Constant;

/**
 * Implement cơ sở thực hiện thao tác DB đóng mở kết nối với
 * 
 * @author Trần Bá Đạt
 *
 */
public class BaseImpl extends AsyncTask implements BaseInter {
	// đối tượng conn kiểu Connecttion để thực hiện kết nối tới database
	protected Connection conn;
	// Tạo đối tượng ps truy vấn
	protected PreparedStatement ps;
	// Tạo đối tượng rs để trỏ đến một hàng của một bảng
	protected ResultSet rs;

	/**
	 * Override phương thực mở kết nối openConnection Thực hiện kết nối đến cơ sở
	 * dữliệu
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void openConnection() throws SQLException, ClassNotFoundException {
		try {
			// driver sử dụng cho cơ sở dữ liệu
			String jdbc = Constant.JDBC_SQL;
			// tên đăng nhập vào DB
			String user = Constant.USER_SQL;
			// mật khẩu đăng nhập vào DB
			String pass = Constant.PASS_SQL;
			// url của DB
			String URL = Constant.URL_SQL;
			Class.forName(jdbc); // nạp driver
			conn = DriverManager.getConnection(URL, user, pass); // kết nối tới database

		} catch (ClassNotFoundException | SQLException e) {
			// Ghi log
			Logger.getLogger(BaseInter.class.getName()).log(Level.SEVERE, null, e);// ghi log
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
			throw e;
		}
	}

	/**
	 * Override Phương thức đóng kết nối closeConnection Thực hiện việc ngắt kết nối
	 * với cơ sở dữ liệu
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Override
	public void closeConnection() throws SQLException, ClassNotFoundException {
		try {
			// Kiểm tra chưa đóng kết nối
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// ghi log
			Logger.getLogger(BaseInter.class.getName()).log(Level.SEVERE, null, e);
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
			throw e;
		}
	}


	@Override
	protected Connection doInBackground(Object[] objects) {
		try {
			openConnection();
		}catch (Exception e){

		}
		return conn;
	}
}
