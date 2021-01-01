/**
 * Copyright(C) 2020 Hust
 * Room.java Oct 25, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.entities;

import java.io.Serializable;

/**
 * Class chứa thuộc tính của bảng Room
 *
 * @author Trần Bá Đạt
 */
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;
    private int roomId;
    private String roomName;

    public Room() {

    }

    public Room(int roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

}
