/**
 * Copyright(C) 2020 Hust
 * RoomImp.java Oct 25, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.base.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hust.bloddpressure.model.base.RoomInter;
import com.hust.bloddpressure.model.entities.Room;

/**
 * Class thực hiệnt ao tác tơi DB bảng tbl_room
 * 
 * @author Trần Bá Đạt
 *
 */
public class RoomImpl extends BaseImpl implements RoomInter {

	@Override
	public ArrayList<Room> getListRoom() throws ClassNotFoundException, SQLException {
		// Tạo mộ arrayList chứa kết quả là các room
		ArrayList<Room> listGroup = new ArrayList<>();
		try {
			// Mở kết nối
			openConnection();
			// Tạo đối tượng StringBuilder chứa câu truy vấn
			StringBuilder sqlSelect = new StringBuilder();
			// Appen câu truy vấn
			sqlSelect.append("SELECT r.roomid, r.roomname FROM tbl_room r ");
			sqlSelect.append("ORDER BY r.roomid ASC;");
			if (conn != null) {
				// Thực hiện truy vấn
				ps = conn.prepareStatement(sqlSelect.toString());
				rs = ps.executeQuery();
				// Thực hiện lấy kết quả
				while (rs.next()) {
					// Tạo đối tượng room
					Room room = new Room();
					int index = 1;
					// Lấy thông tin room lưu vào entities
					room.setRoomId(rs.getInt(index++));
					room.setRoomName(rs.getString(index));
					// Add group vào danh sách group
					listGroup.add(room);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			// ghi log
			Logger.getLogger(RoomImpl.class.getName()).log(Level.SEVERE, null, e);
			// Ném ra một ngoại lệ ra bên ngoài
			throw e;
		} finally {
			// Đóng kết nối
			closeConnection();
		}
		// Trả về danh sách group
		return listGroup;
	}

	@Override
	public String getRoomById(int roomId) throws ClassNotFoundException, SQLException {
		// Tạo string chứa tên cần lấy
		String roomName = null;
		try {
			// Mở kết nối
			openConnection();
			// Tạo đối tượng StringBuilder chứa câu truy vấn
			StringBuilder sqlSelect = new StringBuilder();
			// Appen câu truy vấn
			sqlSelect.append("SELECT r.roomname FROM tbl_room r ");
			sqlSelect.append("WHERE r.roomid = ?;");
			if (conn != null) {
				// Chuẩn bị truy vấn
				ps = conn.prepareStatement(sqlSelect.toString());
				int index = 1;
				// Set giá trị truy vấn truyền vào
				ps.setInt(index, roomId);
				// Thực hiện truy vấn
				rs = ps.executeQuery();
				while (rs.next()) {
					roomName = rs.getString("roomname");
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			// Ghi log
			Logger.getLogger(RoomImpl.class.getName()).log(Level.SEVERE, null, e);
			// Ném một ngoại lệ ra bên ngoài
			throw e;
		} finally {
			// đóng kết nối
			closeConnection();
		}
		// Trả về tên room
		return roomName;
	}

	@Override
	public boolean deleteRoomById(int roomId) throws ClassNotFoundException, SQLException {
		boolean result = false;
		try {
			// Chưa check trường hơp phòng đang có người
			int userid = getUserIdByRoomId(roomId);
			if (userid != 0) {
				// mở kết nối
				openConnection();
				if (conn != null) {
					// Khởi tạo đối tượng chứa câu truy vấn
					StringBuilder sqlUpdate = new StringBuilder();
					// Append câu truy vấn
					sqlUpdate.append("DELETE FROM tbl_room");
					sqlUpdate.append(" WHERE roomid = ?;");
					// Thực hiện truyền câu truy vấn
					ps = conn.prepareStatement(sqlUpdate.toString());
					// Set các giá trị tham số truyền vào cho câu truy vấn
					int index = 1;
					ps.setInt(index++, roomId);
					// Kiểm tra update thành công
					if (ps.executeUpdate() > 0) {
						result = true;
					}
				}
			}
		} catch (SQLException e) {
			// ghi log
			Logger.getLogger(RoomImpl.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			// đóng kết nối
			closeConnection();
		}
		return result;

	}

	/**
	 * Kiểm tra phòng có còn người không
	 * 
	 * @param roomId id của phòng
	 * @return true nếu tồn tại, false nếu không tồn tại
	 */
	private int getUserIdByRoomId(int roomId) {
		int userid = 0;
		try {
			openConnection();
			if (conn != null) {
				StringBuilder sqlSelect = new StringBuilder();
				sqlSelect.append(
						"SELECT u.userid FROM tbl_user u INNER JOIN tbl_room r ON u.roomid = r.roomid WHERE r.roomid = ?");
				ps = conn.prepareStatement(sqlSelect.toString());
				ps.setInt(1, roomId);
				rs = ps.executeQuery();
				if (rs.next()) {
					userid = rs.getInt("userid");
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return userid;
	}

	@Override
	public boolean insertNewRoom(String roomName) throws ClassNotFoundException, SQLException {
		boolean rs = false;
		try {
			openConnection();
			if (conn != null) {
				// tạo câu truy vấn
				StringBuilder sqlInsert = new StringBuilder();
				sqlInsert.append("INSERT INTO tbl_room(roomname) VALUES(?)");
				// Thực hiện truyền câu truy vấn
				ps = conn.prepareStatement(sqlInsert.toString());
				// set các giá trị cho tham số truyền vào
				int index = 1;
				ps.setString(index, roomName);
				// Thực hiện insert vào DB
				if (ps.executeUpdate() > 0) {
					rs = true;
				}

			}
		} catch (Exception e) {
			Logger.getLogger(RoomImpl.class.getName()).log(Level.SEVERE, null, e);// ghi log
		} finally {
			closeConnection();
		}
		return rs;
	}

	@Override
	public boolean updateRoom(int roomId, String newRoomName) throws ClassNotFoundException, SQLException {
		boolean result = false;
		try {
			openConnection();
			if (conn != null) {
				// Khởi tạo đối tượng chứa câu truy vấn
				StringBuilder sqlUpdate = new StringBuilder();
				// Append câu truy vấn
				sqlUpdate.append("UPDATE tbl_room SET roomname =?");
				sqlUpdate.append(" WHERE roomid = ?;");
				// Thực hiện truyền câu truy vấn
				ps = conn.prepareStatement(sqlUpdate.toString());
				// Set các giá trị tham số truyền vào cho câu truy vấn
				int index = 1;
				ps.setString(index++, newRoomName);
				ps.setInt(index, roomId);
				// Kiểm tra update thành công
				if (ps.executeUpdate() > 0) {
					result = true;
				}
			}
		} catch (SQLException e) {
			// ghi log
			Logger.getLogger(RoomImpl.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			// đóng kết nối
			closeConnection();
		}
		return result;
	}

	public static void main(String[] args) {
		RoomImpl roomImpl = new RoomImpl();
		try {
//			roomImpl.insertNewRoom("Phong noi tru them");
			System.out.println(roomImpl.getUserIdByRoomId(2));
//			roomImpl.deleteRoomById(4);
//			roomImpl.updateRoom(1, "Phong kham noi khoa2");
//			ArrayList<Room> lisRooms = roomImpl.getListRoom();
//			for (Room room : lisRooms) {
//				System.out.println(room.getRoomName());
//			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
