/**
 * Copyright(C) 2020 Hust
 * UserInfor.java Oct 25, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Trần Bá Đạt
 */
public class UserInfor implements Serializable {
    /**
     * Class chứa thuộc tính cần lấy ra trong các bảng
     */
    private static final long serialVersionUID = 1L;
    private String userId;
    private int roomId;
    private int age;
    private int rule;
    private int pressureMin;
    private int pressureMax;
    private int predictType;
    private int heartBeat;
    private int systolicMax;
    private int systolicMin;
    private int diastolicMax;
    private int diastolicMin;
    private int pressureId;

    private String fullName;
    private String tel;
    private String room;
    private String diseaseName;
    private String salt;
    private String username;
    private String password;

    private Date date;

    public UserInfor() {
    }

    public UserInfor(String userId, int roomId, int age, int predictType, int systolicMax, int diastolicMin, int pressureId, int pressureMax, int pressureMin, int heartBeat, String fullName, String tel, String room, String diseaseName, Date date) {
        this.userId = userId;
        this.roomId = roomId;
        this.age = age;
        this.pressureMin = pressureMin;
        this.pressureMax = pressureMax;
        this.predictType = predictType;
//        this.diastolicMax = diastolicMax;
        this.systolicMax = systolicMax;
//        this.systolicMin = systolicMin;
        this.diastolicMin = diastolicMin;
        this.pressureId = pressureId;
        this.heartBeat = heartBeat;
        this.fullName = fullName;
        this.tel = tel;
        this.room = room;
        this.diseaseName = diseaseName;
        this.date = date;
    }


    public UserInfor(String userId, int roomId, int age, int rule, int pressureMin, int pressureMax, int predictType, int heartBeat, String fullName, String tel, String room, String diseaseName, String username) {
        this.userId = userId;
        this.roomId = roomId;
        this.age = age;
        this.rule = rule;
        this.pressureMin = pressureMin;
        this.pressureMax = pressureMax;
        this.predictType = predictType;
        this.heartBeat = heartBeat;
        this.fullName = fullName;
        this.tel = tel;
        this.room = room;
        this.diseaseName = diseaseName;
        this.username = username;
    }

    public UserInfor(String userId, int roomId, int age, int rule, int predictType, String fullName, String tel, String room, String diseaseName, String username) {
        this.userId = userId;
        this.roomId = roomId;
        this.age = age;
        this.rule = rule;
        this.predictType = predictType;
        this.fullName = fullName;
        this.tel = tel;
        this.room = room;
        this.diseaseName = diseaseName;
        this.username = username;
    }

    public UserInfor(String userId, int rule, String salt, String username, String password) {
        this.userId = userId;
        this.rule = rule;
        this.salt = salt;
        this.username = username;
        this.password = password;
    }

    public UserInfor(String userId, String fullName, int age, String diseaseName) {
        this.userId = userId;
        this.fullName = fullName;
        this.age = age;
        this.diseaseName = diseaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getPredictType() {
        return predictType;
    }

    public void setPredictType(int predictType) {
        this.predictType = predictType;
    }

    public int getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(int pressureMin) {
        this.pressureMin = pressureMin;
    }

    public int getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(int pressureMax) {
        this.pressureMax = pressureMax;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }
    public int getSystolicMax() {
        return systolicMax;
    }

    public void setSystolicMax(int systolicMax) {
        this.systolicMax = systolicMax;
    }

    public int getSystolicMin() {
        return systolicMin;
    }

    public void setSystolicMin(int systolicMin) {
        this.systolicMin = systolicMin;
    }

    public int getDiastolicMax() {
        return diastolicMax;
    }

    public void setDiastolicMax(int diastolicMax) {
        this.diastolicMax = diastolicMax;
    }

    public int getDiastolicMin() {
        return diastolicMin;
    }

    public void setDiastolicMin(int diastolicMin) {
        this.diastolicMin = diastolicMin;
    }

    public int getPressureId() {
        return pressureId;
    }

    public void setPressureId(int pressureId) {
        this.pressureId = pressureId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
