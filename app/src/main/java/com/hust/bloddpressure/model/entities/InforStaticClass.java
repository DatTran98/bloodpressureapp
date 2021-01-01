package com.hust.bloddpressure.model.entities;

public class InforStaticClass {
    private static int rule;
    private static String userId;
    private static String username;
    private static String password;

    public static void setRule(int rule) {
        InforStaticClass.rule = rule;
    }

    public static void setUserId(String userId) {
        InforStaticClass.userId = userId;
    }

    public static int getRule() {
        return rule;
    }

    public static String getUserId() {
        return userId;
    }
}
