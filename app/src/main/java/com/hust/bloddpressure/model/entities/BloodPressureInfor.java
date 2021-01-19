/**
 * Copyright(C) 2020 Hust
 * BloodPressureInfor.java Oct 24, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.model.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Trần Bá Đạt
 */
public class BloodPressureInfor implements Serializable {

    private static final long serialVersionUID = 1L;
    private int bloodPressureId;
    private int pressureMax;
    private int pressureMin;
    private int heartBeat;
    private int standardMax;
    private int standardMin;
    private Date time;


    public BloodPressureInfor(int bloodPressureId, int pressureMax, int pressureMin, int heartBeat, int standardMax, int standardMin, Date time) {
        this.bloodPressureId = bloodPressureId;
        this.pressureMax = pressureMax;
        this.pressureMin = pressureMin;
        this.heartBeat = heartBeat;
        this.standardMax = standardMax;
        this.standardMin = standardMin;
        this.time = time;
    }

    public BloodPressureInfor() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPressureMin() {
        return pressureMin;
    }

    public void setPressureMin(int pressureMin) {
        this.pressureMin = pressureMin;
    }

    public int getBloodPressureId() {
        return bloodPressureId;
    }

    public void setBloodPressureId(int bloodPressureId) {
        this.bloodPressureId = bloodPressureId;
    }

    public int getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(int pressureMax) {
        this.pressureMax = pressureMax;
    }

    public int getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(int heartBeat) {
        this.heartBeat = heartBeat;
    }

    public int getStandardMax() {
        return standardMax;
    }

    public void setStandardMax(int standardMax) {
        this.standardMax = standardMax;
    }

    public int getStandardMin() {
        return standardMin;
    }

    public void setStandardMin(int standardMin) {
        this.standardMin = standardMin;
    }
}
