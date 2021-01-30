/**
 * Copyright(C) 2020 Hust
 * Common.java Oct 27, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.util;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hust.bloddpressure.R;
import com.hust.bloddpressure.model.entities.News;
import com.hust.bloddpressure.model.entities.Room;
import com.hust.bloddpressure.model.entities.UserInfor;

/**
 * Chứa các phương thức dùng chung của project
 *
 * @author Trần Bá Đạt
 */
public class Common {

    /**
     * Kiểm tra chuỗi rỗng
     *
     * @param string chuỗi cần được kiểm tra
     * @return trả về true nếu chuỗi rỗng, false nếu chuỗi khác rỗng
     */
    public static boolean checkEmpty(String string) {
        if ("".equals(string)) {
            return true;
        } else
            return false;
    }

    /**
     * Mã hóa password theo SHA-1
     *
     * @param salt     chuỗi được tạo ngẫu nhiên
     * @param password mật khẩu chưa mã hóa
     * @return trả về mật khẩu đã được mã hóa theo SHA-1
     */
    public static String encrypt(String password, String salt) {
        String passSalt = salt + password;
        try {
            MessageDigest md = MessageDigest.getInstance(Constant.SHA_1);
            byte[] bytePassSalt = md.digest(passSalt.getBytes());
            return convertByteToHex(bytePassSalt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert mảng byte của chuỗi mã hóa sang hệ 16
     *
     * @param data mảng bytes chuỗi mã hóa
     * @return trả về chuỗi đã được convert sang hệ 16
     */
    private static String convertByteToHex(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * Tạo chuỗi salt ngẫu nhiên ngẫu nhiên lấy thời gian tới milisecond của hệ
     * thống
     *
     * @return trả về chuỗi được tạo
     */
    public static String createSalt() {
        // Lấy giá trị thời gian từ hệ thống
        String salt = String.valueOf(System.currentTimeMillis());
        // Trả về salt vừa tạo được
        return salt;
    }
//
//    /**
//     * Xử lý các toán tử wildcard để tránh injection
//     *
//     * @param wildcarString chuỗi chứa các ký tự wildcard
//     * @return chuỗi đã mã hóa các ký tự wicard
//     */
//    public static String replaceWildCard(String wildcarString) {
//        String encode = wildcarString;
//        if (wildcarString != null && !"".equals(wildcarString)) {
//            encode = encode.replace("\\", "\\\\");
//            encode = encode.replace("%", "\\%");
//            encode = encode.replace("_", "\\_");
//
//        }
//        return encode;
//    }

    /**
     * Check độ dài một chuỗi trong khoảng min đến max
     *
     * @param strCheck        chuỗi cần kiểm tra
     * @param minLengthString độ dài nhỏ nhất
     * @param strCheck        độ dài lớn nhất
     * @return true nếu chuỗi thỏa mãn, false nếu không thỏa mãn
     */
    public static boolean checkLength(String strCheck, int minLengthString, int maxLengthString) {
        if (strCheck.length() < minLengthString || strCheck.length() > maxLengthString) {
            return true;
        } else {
            return false;
        }
    }
//
//    /**
//     * Phương thức so sánh 2 chuỗi
//     *
//     * @param string1 chuỗi thứ nhất
//     * @param string2 chuỗi thứ 2
//     * @return trả về true nếu 2 chuỗi bằng nhau, false nếu khác nhau
//     */
//    private static boolean compareString(String string1, String string2) {
//        boolean check = false;
//        if (string1.equals(string2)) {
//            check = true;
//        }
//        return check;
//    }

    /**
     * Phương thức validate phần Login
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return errorList Trả về một danh sách lỗi
     */
    public static String validateLogin(String username, String password) {
        String error = "";
        // check empty and length of input text
        if (Common.checkEmpty(username)) {
            // attach error message
            error += Constant.EMPTY_MESSAGE + Constant.USERNAME_NAME;
        } else if (Common.checkEmpty(password)) {
            error += Constant.EMPTY_MESSAGE + Constant.PASSWORD_NAME;
        } else if (checkLength(username, Constant.USER_MIN_LENGTH, Constant.MAX_LENGTH)) {
            error += Constant.NOTE + Constant.USER_MIN_LENGTH + " <= " + Constant.USERNAME_NAME + " <= " + Constant.MAX_LENGTH;
        } else if (checkLength(password, Constant.MIN_LENGTH, Constant.MAX_LENGTH))
            error += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.PASSWORD_NAME + " <= " + Constant.MAX_LENGTH;
        return error;
    }

    public static int convertToInt(String toString, int i) {
        int parse = 0;
        try {
            parse = Integer.parseInt(toString);
        } catch (NumberFormatException e) {
            parse = i;
        }
        return parse;
    }

    public static String validateUser(UserInfor userInfor, int mode) {
        String noteError = "";
        if (Constant.MODE_EDIT == mode) {
            if (checkEmpty(userInfor.getFullName())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.FULL_NAME_NAME;
            } else if (userInfor.getAge() <= 0) {
                noteError += Constant.EMPTY_MESSAGE + Constant.AGE_NAME;
            } else if (checkEmpty(userInfor.getDiseaseName())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.DISEASE_NAME_NAME;
            } else if (checkEmpty(userInfor.getTel())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.TEL_NAME;
            } else if (checkLength(userInfor.getUserId(), Constant.MIN_LENGTH, Constant.ID_MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.USER_ID_NAME + " <= " + Constant.ID_MAX_LENGTH;
            } else if (checkLength(userInfor.getFullName(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.FULL_NAME_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (checkMinAge(userInfor.getAge(), Constant.MIN_AGE)) {
                noteError += Constant.NOTE + Constant.AGE_NAME + " >= " + Constant.MIN_AGE;
            } else if (checkLength(userInfor.getDiseaseName(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.DISEASE_NAME_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (checkLength(userInfor.getTel(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.TEL_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (userInfor.getAge() < 0) {
//                noteError += Constant.NOTE + Constant.ROOM_NOT_EXIST;
                userInfor.setRoomId(Constant.INT_VALUE_DEFAULT);
            }
            // check room?
        } else if (Constant.MODE_ADD == mode) {
            if (checkEmpty(userInfor.getUserId())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.USER_ID_NAME;
            } else if (checkEmpty(userInfor.getFullName())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.FULL_NAME_NAME;
            } else if (checkEmpty(userInfor.getUsername())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.USERNAME_NAME;
            } else if (checkEmpty(userInfor.getPassword())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.PASSWORD_NAME;
            } else if (userInfor.getAge() <= 0) {
                noteError += Constant.EMPTY_MESSAGE + Constant.AGE_NAME;
            } else if (checkEmpty(userInfor.getDiseaseName())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.DISEASE_NAME_NAME;
            } else if (checkEmpty(userInfor.getTel())) {
                noteError += Constant.EMPTY_MESSAGE + Constant.TEL_NAME;
            } else if (checkLength(userInfor.getUserId(), Constant.MIN_LENGTH, Constant.ID_MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.USER_ID_NAME + " <= " + Constant.ID_MAX_LENGTH;
            } else if (checkLength(userInfor.getFullName(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.FULL_NAME_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (checkLength(userInfor.getUsername(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.USERNAME_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (checkLength(userInfor.getPassword(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.PASSWORD_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (checkMinAge(userInfor.getAge(), Constant.MIN_AGE)) {
                noteError += Constant.NOTE + Constant.AGE_NAME + " >= " + Constant.MIN_AGE;
            } else if (checkLength(userInfor.getDiseaseName(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.DISEASE_NAME_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (checkLength(userInfor.getTel(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += Constant.NOTE + Constant.MIN_LENGTH + " <= " + Constant.TEL_NAME + " <= " + Constant.MAX_LENGTH;
            } else if (userInfor.getRoomId() == -1) {
                userInfor.setRoomId(Constant.INT_VALUE_DEFAULT);
            }
        }
        return noteError;
    }

    private static boolean checkMinAge(int age, int minAge) {
        if (age < age) {
            return true;
        } else return false;
    }


    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static String validateAddRoom(Room room) {
        String noteError = "";
        int roomId = room.getRoomId();
        String roomName = room.getRoomName();
        if (roomId == 0) {
            noteError += Constant.EMPTY_MESSAGE + Constant.ROOM_ID_NAME;
        } else if (checkEmpty(roomName)) {
            noteError += Constant.EMPTY_MESSAGE + Constant.ROOM_N_NAME;
        }
        return noteError;
    }

    public static String validateAddNew(News news) {
        String noteError = "";
         String title = news.getTitleNew();
        String content = news.getContentNew();
        if (checkEmpty(title)) {
            noteError += Constant.EMPTY_MESSAGE + Constant.TITLE_NAME;
        } else if (checkEmpty(content)) {
            noteError += Constant.EMPTY_MESSAGE + Constant.CONTENT_NAME;
        }
        return noteError;
    }
//
//    /**
//     * Set fragment for content view
//     *
//     * @param idFrameContent  id của frame cần set
//     * @param fragmentRequire fragment cần set vào layout
//     */
//    public static void setFragmentByManagerFragment(int idFrameContent, Fragment fragmentRequire, AppCompatActivity activity) {
//        FragmentManager fragmentManager = activity.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(idFrameContent, fragmentRequire);
//        fragmentTransaction.commit();
//    }
//
//    public static void backNormal(int position, View item, View viewChoose) {
//        if (position % 2 == 0) {
//            item.setBackgroundResource(R.drawable.view_item_custom_odd);
//        } else {
//            item.setBackgroundResource(R.drawable.view_item_custom_even);
//        }
//        viewChoose.setVisibility(View.INVISIBLE);
//    }
}
