/**
 * Copyright(C) 2020 Hust
 * BaseDao.java 24 Oct 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.base;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface cơ sở thực hiện thao tác DB đóng mở kết nối với
 * 
 * @author Trần Bá Đạt
 */
public interface BaseInter {
	/**
	 * Thực hiện kết nối đến cơ sở dữ liệu
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void openConnection() throws SQLException, ClassNotFoundException;

	/**
	 * Thực hiện việc ngắt kết nối với cơ sở dữ liệu
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void closeConnection() throws SQLException, ClassNotFoundException;
	

}
