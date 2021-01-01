/**
 * Copyright(C) 2020 Hust
 * TblUserDao.java Oct 24, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.base;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hust.bloddpressure.model.entities.BloodPressureInfor;
import com.hust.bloddpressure.model.entities.UserInfor;

/**
 * Interface thao tác database với bảng tbl_user
 * 
 * @author Trần Bá Đạt
 *
 */
public interface TblUserInter extends BaseInter {
	/**
	 * Thêm mới một user và tạo bảng thông tin lịch sử đo huyết áp
	 * 
	 * @param userInfor Đối tượng chứa thông tin user cần thêm mới
	 * @return true nếu thành công, fale nếu thất bại
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean createUser(UserInfor userInfor) throws ClassNotFoundException, SQLException;

//	public TblUserEntities getTblUserById(int userId) throws SQLException, ClassNotFoundException;
//
//	/**
//	 * Lấy ra đối tượng userInfor theo Id
//	 * 
//	 * @param userId id cần lấy
//	 * @return user đối tượng userInfor cần lấy
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 */
//	public void updateTblUser(TblUserEntities tblUser) throws ClassNotFoundException, SQLException;
//

	/**
	 * Lấy thông tin đo huyết áp lần gần nhất của người dùng
	 * 
	 * @param userid id của user cần lấy thông tin huyết áp
	 * @return BloodPressureInfor đối tượng chứa tông tin huyết áp cần lấy
	 */
	public BloodPressureInfor getRecentlyUpdatedMeasurentById(int userid) throws ClassNotFoundException, SQLException;

	/**
	 * Lấy thông tin đo huyết áp của người dùng
	 * 
	 * @param userid id của user cần lấy thông tin huyết áp
	 * @return BloodPressureInfor đối tượng chứa tông tin huyết áp cần lấy
	 */
	public ArrayList<BloodPressureInfor> getRMeasurentById(int userid) throws ClassNotFoundException, SQLException;

	/**
	 * Lấy thông tin danh sách user
	 * 
	 * @param roomid   id phòng của user
	 * @param fullname tên user
	 * @param sortType kiểu sort ( mặc định sort theo fullname)
	 * @return Danh sách user hiện tại
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ArrayList<UserInfor> getListUser(int roomid, String fullname, String sortType)
			throws ClassNotFoundException, SQLException;

	/**
	 * Lấy thông tin chi tiết user
	 * 
	 * @param userid id user cần lấy thông tin
	 * @return Đối tượng User chứa thông tin
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public UserInfor getDetailUserById(int userid) throws ClassNotFoundException, SQLException;

	/**
	 * Thiết lập kết nối cho tblUserDao
	 * 
	 * @param connection một kết nối
	 */
	public void setConnection(Connection connection);

	/**
	 * Thực hiện xóa 1 tbluser trong bảng tbl_user
	 * 
	 * @param userId id user cần xóa
	 * @return true nếu thành công, false nếu thất bại
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public boolean deleteUserById(int userId) throws ClassNotFoundException, SQLException;

//
//	/**
//	 * Lấy về một kết nối
//	 * 
//	 * @return một kết nối
//	 */
//	public Connection getConnection()throws ClassNotFoundException, SQLException;
//
//	/**
//	 * Thiết lập tắt auto commit
//	 * 
//	 * @throws SQLException
//	 */
//	public void disibledAutoCommit() throws SQLException;
	/**
	 * Lấy tên user theo username và userid
	 * 
	 * @param userid   id của user
	 * @param username tên đăng nhập của user
	 * @return tên của user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String getUserFullNameByUserIdAndUserName(int userid, String username)
			throws ClassNotFoundException, SQLException;
	/**
	 * Xóa thông tin dữ liệu trong bảng lưu kết quả đo
	 * @param userid id của user cần xóa bảng
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void deleteResultPressureInforById(int userid)throws ClassNotFoundException, SQLException;
	/**
	 * Get user theo username
	 * @param username tên đăng nhập user cần lấy	
	 * @return Đối tượng userinfor chứa thông tin cần lấy
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public UserInfor getUserByUsername(String username) throws ClassNotFoundException, SQLException;

}
