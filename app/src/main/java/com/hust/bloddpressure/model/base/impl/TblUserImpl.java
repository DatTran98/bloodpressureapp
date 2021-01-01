/**
 * Copyright(C) 2020 Hust
 * Connect.java Oct 24, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.base.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.jdbc.Statement;

import com.hust.bloddpressure.model.base.TblUserInter;
import com.hust.bloddpressure.model.entities.BloodPressureInfor;
import com.hust.bloddpressure.model.entities.UserInfor;
import com.hust.bloddpressure.util.Constant;

/**
 * @author Trần Bá Đạt
 *
 */
public class TblUserImpl extends BaseImpl implements TblUserInter {

	/**
	 * Tạo bảng lịch sử đo huyết áp cho user theo id
	 * 
	 * @param userid id của user cần tạo
	 * @return true nếu tạo thành công, false nếu thất bại
	 */
	private void createTableResultPressureById(int userid) throws SQLException, ClassNotFoundException {

		try {
			if (conn != null) {
				StringBuilder sqlCreate = new StringBuilder();
				sqlCreate.append("CREATE TABLE tbl_result_pressure_histories_" + userid
						+ " (blood_pressure_id INT(9) NOT NULL AUTO_INCREMENT PRIMARY KEY,"
						+ " pressure_max INT(9),\r\n" + "    pressure_min INT(9), time DATETIME)");
				// Tạo đối tượng truy vấn
				Statement stmp = (Statement) conn.createStatement();
				// Thực hiện truy vấn
				stmp.executeUpdate(sqlCreate.toString());
			}
		} catch (SQLException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log

		}
	}

	@Override
	public BloodPressureInfor getRecentlyUpdatedMeasurentById(int userid) throws ClassNotFoundException, SQLException {
		// Tạo một đối tượng để chứa dữ liệu huyết áp trả về
		BloodPressureInfor bloodPressureInfor = new BloodPressureInfor();
		try {
			// Mở kết nối
			openConnection();
			if (conn != null) {
				// Tạo câu lệnh select
				StringBuilder sqlSelectName = new StringBuilder();
				sqlSelectName.append(
						"SELECT * FROM tbl_result_pressure_histories_?" + " ORDER BY blood_pressure_id DESC LIMIT 1");
				// Thực hiện lấy câu lệnh truy vấn
				ps = (PreparedStatement) conn.prepareStatement(sqlSelectName.toString());
				// Set điều kiện cho câu truy vấn
				ps.setInt(1, userid);
				// thực hiện truy vấn
				rs = ps.executeQuery();
				// lấy danh sách kết quả
				while (rs.next()) {
					// Lấy kết quả theo và set cho đối tượng
					bloodPressureInfor.setBloodPressureId(rs.getInt("blood_pressure_id"));
					bloodPressureInfor.setPressureMax(rs.getInt("pressure_max"));
					bloodPressureInfor.setPressureMin(rs.getInt("pressure_min"));
					bloodPressureInfor.setTime(Timestamp.valueOf(rs.getTimestamp("time").toString()));
				}
			}
		} catch (ClassNotFoundException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
		} catch (SQLException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
		} finally {
			closeConnection(); // đóng kết nối
		}
		// Trả về một danh sách chứa các tên user đã tìm được
		return bloodPressureInfor;
	}

	@Override
	public ArrayList<BloodPressureInfor> getRMeasurentById(int userid) throws ClassNotFoundException, SQLException {
		// Tạo một đối tượng list để chứa đối tượng dữ liệu huyết áp trả về
		ArrayList<BloodPressureInfor> listBloodPressureInfors = new ArrayList<BloodPressureInfor>();

		try {
			// Mở kết nối
			openConnection();
			if (conn != null) {
				// Tạo câu lệnh select
				StringBuilder sqlSelectName = new StringBuilder();
				sqlSelectName.append("SELECT * FROM tbl_result_pressure_histories_?");
				// Thực hiện lấy câu lệnh truy vấn
				ps = (PreparedStatement) conn.prepareStatement(sqlSelectName.toString());
				// Set điều kiện cho câu truy vấn
				ps.setInt(1, userid);
				// thực hiện truy vấn
				rs = ps.executeQuery();
				// lấy danh sách kết quả
				while (rs.next()) {
					BloodPressureInfor bloodPressureInfor = new BloodPressureInfor();
					// Lấy kết quả theo và set cho đối tượng
					bloodPressureInfor.setBloodPressureId(rs.getInt("blood_pressure_id"));
					bloodPressureInfor.setPressureMax(rs.getInt("pressure_max"));
					bloodPressureInfor.setPressureMin(rs.getInt("pressure_min"));
					bloodPressureInfor.setTime(Timestamp.valueOf(rs.getTimestamp("time").toString()));
					listBloodPressureInfors.add(bloodPressureInfor);
				}
			}
		} catch (ClassNotFoundException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} catch (SQLException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			closeConnection(); // đóng kết nối
		}
		// Trả về một danh sách chứa các tên user đã tìm được
		return listBloodPressureInfors;

	}

	@Override
	public UserInfor getDetailUserById(int userid) throws ClassNotFoundException, SQLException {
		UserInfor userInfor = null;
		try {
			// Mở kết nối
			openConnection();
			if (conn != null) {
				// Tạo đối tượng chứa câu truy vấn
				StringBuilder sqlSelect = new StringBuilder();
				// Appen câu truy vấn
				sqlSelect.append("SELECT u.userid, u.fullname, u.age, u.tel, u.rule, u.diseasename, ");
				sqlSelect.append(" r.roomid, r.roomname ");
				sqlSelect.append("FROM tbl_room r INNER JOIN tbl_user u ON r.roomid = u.roomid ");
				sqlSelect.append("WHERE u.rule = 1 and u.userid = ?;");
				// Thực hiện truyền câu truy vấn
				ps = conn.prepareStatement(sqlSelect.toString());
				// Set giá trị tham số truyền vào câu truy vấn
				int index = 1;
				ps.setInt(index, userid);
				// Thực hiện truy vấn
				rs = ps.executeQuery();
				// Lấy kết quả
				if (rs.next()) {
					// khởi tạo mộit user để chứa giá trị
					userInfor = new UserInfor();
					// Lấy các giá trị và set các giá trị cho userinfor
					userInfor.setUserId(rs.getString("userid"));
					userInfor.setAge(rs.getInt("age"));
					userInfor.setRoomId(rs.getInt("roomid"));
					userInfor.setRule(rs.getInt("rule"));
					userInfor.setFullname(rs.getString("fullname"));
					userInfor.setRoom(rs.getString("roomname"));
					userInfor.setDiseaseName(rs.getString("diseasename"));
					userInfor.setTel(rs.getString("tel"));
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			// ghi log
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
			throw e;
		} finally {
			// đóng kết nối
			closeConnection();
		}
		// trả về đối tượng user infor
		return userInfor;
	}

	@Override
	public ArrayList<UserInfor> getListUser(int roomid, String fullname, String sortType)
			throws ClassNotFoundException, SQLException {
		ArrayList<UserInfor> listUsers = new ArrayList<>();

		try {
			// Tạo câu lệnh truy vấn
			StringBuilder sqlSelect = new StringBuilder();
			// Appen câu truy vấn
			sqlSelect.append("SELECT u.userid, u.fullname, u.age, u.tel, u.rule, u.diseasename, ");
			sqlSelect.append(" r.roomid, r.roomname ");
			sqlSelect.append("FROM tbl_room r INNER JOIN tbl_user u ON r.roomid = u.roomid ");
			sqlSelect.append("WHERE u.rule = 1 ");

			// Nếu roomid khác 0 thì appen điều kiện
			if (roomid != 0) {
				sqlSelect.append("AND u.roomid = ? ");
			}

			if (fullname != null && !fullname.isEmpty()) {
				sqlSelect.append("AND u.fullname LIKE ? ");
			}
			// Mở kết nối
			openConnection();
			if (conn != null) {
				// Kiểm tra tồn tại
				if (sortType != null && !sortType.isEmpty()) {
					sqlSelect.append("ORDER BY fullname ");
					if ("DESC".equals(sortType)) {
						sqlSelect.append("DESC");

					}
				}
				ps = conn.prepareStatement(sqlSelect.toString());
				int index = 1;
				// Nếu room khác 0 thì set giá trị điều kiện cho room
				if (roomid != 0) {
					ps.setInt(index++, roomid);
				}
				// Nếu fullname khác null hoặc khác rỗng thì set giá trị vào điều kiện
				if (fullname != null && !fullname.isEmpty()) {
					StringBuilder condition = new StringBuilder();
					condition.append("%" + fullname + "%");
					ps.setString(index++, condition.toString());
				}
				// Thực hiện truy vấn
				rs = ps.executeQuery();
				while (rs.next()) {
					// Khởi tạo đối tượng userInforEntities để lưu thông tin
					UserInfor userInfor = new UserInfor();
					// Lấy các giá trị và set các giá trị cho userinfor
					userInfor.setUserId(rs.getString("userid"));
					userInfor.setAge(rs.getInt("age"));
					userInfor.setRoomId(rs.getInt("roomid"));
					userInfor.setRule(rs.getInt("rule"));
					userInfor.setFullname(rs.getString("fullname"));
					userInfor.setRoom(rs.getString("roomname"));
					userInfor.setDiseaseName(rs.getString("diseasename"));
					userInfor.setTel(rs.getString("tel"));
					// Add user vào list
					listUsers.add(userInfor);
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
			throw e;
		} finally {
			// Đóng kết nối
			closeConnection();
		}
		// Trả về danh sách User
		return listUsers;
	}

	/**
	 * Thêm mới một user
	 * 
	 * @param userInfor Đối tượng chứa thông tin user cần thêm mới
	 * @return id của user vừa tạo
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private int insertUser(UserInfor userInfor) throws ClassNotFoundException, SQLException {
		int userId = 0;
		try {
			if (conn != null) {
				// tạo câu truy vấn lấy id tự tăng cuối cùng
				StringBuilder sqlSelect = new StringBuilder();
				// append câu truy vấn
				sqlSelect.append("SELECT LAST_INSERT_ID();");
				// tạo câu truy vấn để insert
				StringBuilder sqlInsert = new StringBuilder();
				sqlInsert.append("INSERT INTO tbl_user( fullname, age, diseasename, tel, roomid, rule, salt) ");
				sqlInsert.append("VALUES ( ?, ?, ?, ?, ?, ?, ?);");
				// Thực hiện truyền câu truy vấn
				ps = conn.prepareStatement(sqlInsert.toString());

				// set các giá trị cho tham số truyền vào
				int index = 1;
				ps.setString(index++, userInfor.getFullname());
				ps.setInt(index++, userInfor.getAge());
				ps.setString(index++, userInfor.getDiseaseName());
				ps.setString(index++, userInfor.getTel());
				ps.setInt(index++, userInfor.getRoomId());
				ps.setInt(index++, userInfor.getRule());
				ps.setString(index, userInfor.getSalt());
				// Thực hiện insert vào DB
				ps.executeUpdate();

				// Truyền câu truy vấn select
				ps = conn.prepareStatement(sqlSelect.toString());
				// Thực hiện select để lấy ra userId cuối cùng
				rs = ps.executeQuery();
				if (rs.next()) {
					// Lấy giá trị userId
					userId = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		}
		return userId;
	}

	@Override
	public boolean createUser(UserInfor userInfor) throws ClassNotFoundException, SQLException {
		boolean result = false;
		try {
			// Mở kết nối
			openConnection();
			if (conn != null) {
				// Tắt autocommit
				conn.setAutoCommit(false);
				// thêm mới 1 user
				int userId = insertUser(userInfor);
				if (userId != 0) {
					// insert account
					createAccountById(userId, "admin", "12345");
					// Thêm mới 1 bảng lịch sử kết quả đo cho user
					createTableResultPressureById(userId);
					conn.commit();
					result = true;
				}
			}
		} catch (Exception e) {
			conn.rollback();
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			// đóng kết nối
			closeConnection();
		}
		return result;
	}

	@Override
	public void setConnection(Connection connection) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param userId
	 */
	private void deleteTblUserById(int userId) throws ClassNotFoundException, SQLException {
		try {
			// Khởi tạo đối tượng chứa câu truy vấn
			StringBuilder sqlDetele = new StringBuilder();
			// Append câu truy vấn
			sqlDetele.append("DELETE FROM tbl_user WHERE userid = ? and rule = ?;");
			// Truyền câu truy vấn
			ps = conn.prepareStatement(sqlDetele.toString());
			// Set giá trị truyền vào cho câu truy vấn
			int index = 1;
			ps.setInt(index++, userId);
			ps.setInt(index++, Constant.USER_RULE);
			// Thực hiện update và kiểm tra thànhc công
			ps.executeUpdate();
		} catch (SQLException e) {
			// ghi log
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	/**
	 * @param userId
	 */
	private void deleteTblAccountById(int userId) {
		try {
			// Khởi tạo đối tượng chứa câu truy vấn
			StringBuilder sqlDetele = new StringBuilder();
			// Append câu truy vấn
			sqlDetele.append("DELETE FROM tbl_accounts WHERE userid = ?;");
			// Truyền câu truy vấn
			ps = conn.prepareStatement(sqlDetele.toString());
			// Set giá trị truyền vào cho câu truy vấn
			int index = 1;
			ps.setInt(index++, userId);
			// Thực hiện update và kiểm tra thànhc công
			ps.executeUpdate();
		} catch (SQLException e) {
			// ghi log
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	/**
	 * Xóa bảng lịch sử đo huyết áp của user khi xóa user
	 * 
	 * @param userid id của user cần xóa bảng
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void deleteTableResultPressureById(int userid) throws ClassNotFoundException, SQLException {
		StringBuilder sqlDelete = new StringBuilder();
		sqlDelete.append("DROP TABLE tbl_result_pressure_histories_" + userid);
		try {
//			openConnection();
			if (conn != null) {
				// Tạo đối tượng truy vấn
				ps = conn.prepareStatement(sqlDelete.toString());
				// Thực hiện truy vấn
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO: handle exception
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log

		}
	}

	@Override
	public boolean deleteUserById(int userId) throws ClassNotFoundException, SQLException {
		boolean result = false;
		try {
			// Mở kết nối tới DB
			openConnection();
			conn.setAutoCommit(false);
			if (conn != null) {
				conn.setAutoCommit(false);
				deleteTblAccountById(userId);
				deleteTblUserById(userId);
				deleteTableResultPressureById(userId);
				conn.commit();
				result = true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			conn.rollback();
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			closeConnection();
		}
		return result;
	}

	/**
	 * Tạo mới account by id
	 * 
	 * @param userId   id của user
	 * @param userName tên đăng nhập của user
	 * @param password mật khẩu của user
	 * @return true nếu thành công, false nếu thất bại
	 * @throws SQLException
	 */
	private void createAccountById(int userId, String userName, String password) throws SQLException {
		try {
			if (conn != null) {
				// Tạo câu truy vấn
				StringBuilder sqlInsert = new StringBuilder();
				// Appen câu truy vấn
				sqlInsert.append("INSERT INTO tbl_accounts( username, password, userid) VALUES (?,?,?)");
				// Truyền câu truy vấn
				ps = conn.prepareStatement(sqlInsert.toString());
				// truyền tham số cho câu truy vấn
				int index = 1;
				ps.setString(index++, userName);
				ps.setString(index++, password);
				ps.setInt(index, userId);
				// Thực hiện thêm vào DB
				ps.executeUpdate();
			}
		} catch (Exception e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		}
	}

	@Override
	public String getUserFullNameByUserIdAndUserName(int userid, String username)
			throws ClassNotFoundException, SQLException {
		String fullname = null;
		try {
			// Mở một kết nối
			openConnection();
			if (conn != null) {
				// Tạo đối tượng chưa câu truy vấn
				StringBuilder sqlSelect = new StringBuilder();
				// Appen câu truy vấn
				sqlSelect.append("SELECT fullname FROM tbl_user ");
				sqlSelect.append("WHERE username = ? ");
				// Nếu có user id > thì appen
				if (userid > 0) {
					sqlSelect.append("AND user_id != ? ");
				}
				sqlSelect.append(";");
				// Tạo đối tượng truy vấn và lấy câu truy vấn
				ps = conn.prepareStatement(sqlSelect.toString());

				// Thiết lập điều kiện truy vấn
				int index = 1;
				ps.setString(index++, username);
				if (userid > 0) {
					ps.setInt(index, userid);
				}
				// Thực hiện truy vấn
				rs = ps.executeQuery();
				// Lấy kết quả tìm được
				while (rs.next()) {
					fullname = rs.getString("fullname");
				}
			}
		} catch (Exception e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			closeConnection();
		}
		return fullname;
	}

	@Override
	public void deleteResultPressureInforById(int userid) throws ClassNotFoundException, SQLException {
		try {
			openConnection();
			if (conn != null) {
				// Tạo đối tượng chưa câu truy vấn
				StringBuilder sqlDel = new StringBuilder();
				// Appen câu truy vấn
				sqlDel.append("DELETE FROM tbl_result_pressure_histories_" + userid);
				// truyền câu truy vấn
				ps = conn.prepareStatement(sqlDel.toString());
				// truyền điều kiện
				// thực hiện truy vấn
				ps.executeUpdate();
			}

		} catch (Exception e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			closeConnection();
		}
	}

	/**
	 * @see
	 */

	@Override
	public UserInfor getUserByUsername(String username) throws ClassNotFoundException, SQLException {
		// Tạo một đối trường tbluser để trả về
		UserInfor user = new UserInfor();
		// Tạo một câu truy vấn
		StringBuilder sqlSelect = new StringBuilder();
		sqlSelect.append("SELECT a.username, a.password, u.salt, u.rule ");
		sqlSelect.append("FROM tbl_accounts a INNER JOIN tbl_user u ");
		sqlSelect.append("ON a.userid = u.userid ");
		sqlSelect.append("WHERE a.username = ?;");
		try {
			// Kiểm tra tên khác rỗng
			if (!username.isEmpty()) {
				// Tạo một đối tượng và lấy danh sách tên các user có trong db
				ArrayList<String> listUserName = getListUsername();
				// Kiểm tra xem tên user nhập vào có trong db không
				boolean containUser = listUserName.contains(username.toLowerCase());
				// nếu có
				if (containUser) {
					// Mở một kết nối
					openConnection();
					if (conn != null) {
						// Tạo đối tượng truy vấn và lấy câu truy vấn
						ps = conn.prepareStatement(sqlSelect.toString());
						// Thiết lập điều kiện truy vấn
						int index = 1;
						ps.setString(index++, username);
						// Thực hiện truy vấn
						rs = ps.executeQuery();
						// Lấy kết quả tìm được
						while (rs.next()) {
							user.setUsername(rs.getString("username"));
							user.setPassword(rs.getString("password"));
							user.setSalt(rs.getString("salt"));
							user.setRule(rs.getInt("rule"));
						}
					}

				}
			}
		} catch (ClassNotFoundException |SQLException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			closeConnection(); // đóng kết nối
		}
		// Trả về một user sau khi tìm kiếm
		return user;
	}

	/** Lấy danh sách tên đăng nhập
	 * @return Danh sách tên đăng nhấp
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	private ArrayList<String> getListUsername() throws ClassNotFoundException, SQLException {
		// Tạo một đối tượng listUserName để chứa danh sách tên các user
		ArrayList<String> listUserName = new ArrayList<String>();
		try {
			// Mở kết nối
			openConnection();
			if (conn != null) {
				// Tạo câu lệnh select
				StringBuilder sqlSelectName = new StringBuilder();
				sqlSelectName.append("SELECT username FROM tbl_accounts;");
				// Thực hiện lấy câu lệnh truy vấn
				ps = (PreparedStatement) conn.prepareStatement(sqlSelectName.toString());
				// thực hiện truy vấn
				rs = ps.executeQuery();
				// lấy danh sách kết quả
				while (rs.next()) {
					// Lấy kết quả theo trường login_name
					String loginName = rs.getString("username");
					// thêm vào danh sách tên các user
					listUserName.add(loginName.toLowerCase());
				}
			}
		} catch (SQLException e) {
			Logger.getLogger(TblUserImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
			// Ném một ngoại lệ ra ngoài để hàm gọi tới xử lý
		} finally {
			closeConnection();
		}
		// Trả về một danh sách chứa các tên user đã tìm được
		return listUserName;
	}

	public static void main(String[] args) {
		TblUserImpl cn = new TblUserImpl();
		try {
//			ArrayList<String> list =cn.getListUsername();
//			for (String string : list) {
//				System.out.println(string);
//			}
//			cn.deleteUserById(3);
//			System.out.println("success");
//			UserInfor userInfor = new UserInfor();
//			userInfor.setFullname("DatTB");
//			userInfor.setAge(14);
//			userInfor.setRule(1);
//			userInfor.setRoomId(1);
//			userInfor.setTel("0988558");
//			userInfor.setDiseasename("Benh lyrrr");
//			userInfor.setSalt("sal");
//			cn.createUser(userInfor);
//			for (int i = 1; i < 3; i++) {
//				cn.createTableResultPressureById(3);
//			cn.deleteTableResultPressureById(0);
//			cn.deleteResultPressureInforById(11);
// 
//				if (rs) {
//					System.out.println(" Create succeed tbl " + i);
//				}
//			}
			// Tesst lay thong tin do
//			ArrayList<BloodPressureInfor> bloodPressureInfors = cn.getRMeasurentById(1);
//			for (BloodPressureInfor bloodPressureInfor : bloodPressureInfors) {
//				System.out.println(bloodPressureInfor.getBloodPressureId());
//				System.out.println(bloodPressureInfor.getPressureMax());
//				System.out.println(bloodPressureInfor.getPressureMin());
//				System.out.println(bloodPressureInfor.getTime());
//			}
//			BloodPressureInfor bloodPressureInfor = cn.getRecentlyUpdatedMeasurentById(1);
			// Test get thong tin usser
//			ArrayList<UserInfor> lisInfors = cn.getListUser(0, "at", "");
//			for (UserInfor userInfor : lisInfors) {
//				System.out.println(" ID user: " + userInfor.getUserId());
//				System.out.println(" Name user: " + userInfor.getFullname());
//				System.out.println(" Age user: " + userInfor.getAge());
//				System.out.println(" Room user: " + userInfor.getRoom());
//				System.out.println(" Tel user: " + userInfor.getTel());
//				System.out.println(" ID room: " + userInfor.getRoomId());
//				System.out.println(" Rule user: " + userInfor.getRule());
//				System.out.println(" Disea: " + userInfor.getDiseasename());
//
//			}
//			UserInfor userInfor = new UserInfor();
//			userInfor = cn.getDetailUserById(1);
//			System.out.println(" ID user: " + userInfor.getUserId());
//			System.out.println(" Name user: " + userInfor.getFullname());
//			System.out.println(" Age user: " + userInfor.getAge());
//			System.out.println(" Room user: " + userInfor.getRoom());
//			System.out.println(" Tel user: " + userInfor.getTel());
//			System.out.println(" ID room: " + userInfor.getRoomId());
//			System.out.println(" Rule user: " + userInfor.getRule());

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(" Nót Succeses");
		}

	}

}
