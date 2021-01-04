package com.hust.bloddpressure.model.entities;

public class Predict {
    private int typePredict;
    private int age;

    public Predict() {
    }

    public Predict(int typePredict, int age) {
        this.typePredict = typePredict;
        this.age = age;
    }

    public int getTypePredict() {
        return typePredict;
    }


    public void setTypePredict(int typePredict) {
        this.typePredict = typePredict;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
