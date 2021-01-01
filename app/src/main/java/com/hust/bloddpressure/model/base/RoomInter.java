/**
 * Copyright(C) 2020 Hust
 * RoomDao.java Oct 25, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.base;

import java.sql.SQLException;
import java.util.ArrayList;
import com.hust.bloddpressure.model.entities.Room;

/**
 * interface thao tác DB với bảng tbl_room
 * 
 * @author Trần Bá Đạt
 *
 */
public interface RoomInter extends BaseInter {
	/**
	 * Lấy danh sách phòng bệnh
	 * 
	 * @return Danh sách phòng bệnh
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ArrayList<Room> getListRoom() throws ClassNotFoundException, SQLException;

	/**
	 * Lấy thông tin phòng bệnh
	 * 
	 * @return Đối tượng Room chứa thông tin phòng bệnh
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String getRoomById(int roomId) throws ClassNotFoundException, SQLException;

	/**
	 * Xóa phòng bệnh theo id phòng
	 * 
	 * @param roomId id của phòng cần xóa
	 * @return true nếu thành công, false nếu thất bại
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean deleteRoomById(int roomId) throws ClassNotFoundException, SQLException;

	/**
	 * Thêm mới phòng bệnh
	 * 
	 * @param roomName Tên phòng bệnh mới
	 * @return true nếu thành công, false nếu thất bại
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean insertNewRoom(String roomName) throws ClassNotFoundException, SQLException;

	/**
	 * Update thông tin phòng
	 * 
	 * @param roomId      id phòng cần update
	 * @param newRoomName tên mới của phòng update
	 * @return true nếu thành công, false nếu thất bại
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean updateRoom(int roomId, String newRoomName) throws ClassNotFoundException, SQLException;

}
