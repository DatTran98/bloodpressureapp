/**
 * Copyright(C) 2020 Hust
 * Common.java Oct 27, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.util;

import android.app.Activity;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hust.bloddpressure.model.base.impl.TblUserImpl;
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
     * @param salt chuỗi được tạo ngẫu nhiên
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

    /**
     * Xử lý các toán tử wildcard để tránh injection
     *
     * @param wildcarString chuỗi chứa các ký tự wildcard
     * @return chuỗi đã mã hóa các ký tự wicard
     */
    public static String replaceWildCard(String wildcarString) {
        String encode = wildcarString;
        if (wildcarString != null && !"".equals(wildcarString)) {
            encode = encode.replace("\\", "\\\\");
            encode = encode.replace("%", "\\%");
            encode = encode.replace("_", "\\_");

        }
        return encode;
    }

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

    /**
     * Kiểm tra tồn tại user name trường hợp đăng ký, edit thông tin user
     *
     * @param userid   id của user cần kiểm tra
     * @param username tên đăng nhập cần kiểm tra
     * @return true nếu tồn tại, false nếu không tồn tại
     */
    public static boolean checkExistUserName(int userid, String username) throws ClassNotFoundException, SQLException {
        boolean result = false;
        TblUserImpl tblUserImpl = new TblUserImpl();
        try {
            String fullName = tblUserImpl.getUserFullNameByUserIdAndUserName(userid, username);
            if (fullName != null) {
                result = true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, e);
        }

        return result;

    }

    /**
     * Check đăng nhập khi user thực hiện đăng nhập
     *
     * @param username tên đăng nhập người dùng nhập vào
     * @param password mật khẩu người dùng nhập vào
     * @return 0 nếu đăng nhập thất bại, 1 nếu là admin, 2 nếu là user
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static int checkLogin(String username, String password) throws ClassNotFoundException, SQLException {
        int resultRule = 0;
        // Khởi tạo chuỗi salt
        String salt = null;
        // Khởi tạo một đối tượng TblUserDaoImpl
        TblUserImpl userImpl = new TblUserImpl();
        // Lấy đối tượng tblUser với giá trị tên được nhập
        UserInfor user = userImpl.getUserByUsername(username);
        String userameDB = user.getUsername();
        // Nếu đối tượng tblUser tồn tại
        if (userameDB != null && !userameDB.isEmpty()) {
            // Thực hiện lấy chuỗi salt
            salt = user.getSalt();
            // Thực hiện lấy password
            String passwordDB = user.getPassword();
            // Thực hiện mã hóa sha1 password + salt
            String passwordEncode = Common.encrypt(password, salt);
            // So sánh pass nhập vào và pass trong DB
            boolean comparePass = Common.compareString(passwordDB, passwordEncode);
            if (comparePass) {
                // Lấy giá trị rule
                resultRule = user.getRule();
            }
        }
        return resultRule;
    }

    /**
     * Phương thức so sánh 2 chuỗi
     *
     * @param string1 chuỗi thứ nhất
     * @param string2 chuỗi thứ 2
     * @return trả về true nếu 2 chuỗi bằng nhau, false nếu khác nhau
     */
    private static boolean compareString(String string1, String string2) {
        boolean check = false;
        if (string1.equals(string2)) {
            check = true;
        }
        return check;
    }

    /**
     * Phương thức validate phần Login
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return errorList Trả về một danh sách lỗi
     * @throws SQLException
     * @throws NoSuchAlgorithmException
     * @throws ClassNotFoundException
     */
    public static String validateLogin(String username, String password) {
        String error = "";
        // Kiểm tra xem empty và độ dài chuỗi
        if (Common.checkEmpty(username)) {
            // gan loi
            error += " Chưa nhập username";
        } else if (Common.checkEmpty(password)) {
            error += " Chưa nhập password";
        } else if (checkLength(username, Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
            error += "Chú ý " + Constant.USER_MIN_LENGTH + " < Tên đăng nhập < " + Constant.MAX_LENGTH;
        } else if (checkLength(password, Constant.MIN_LENGTH, Constant.MAX_LENGTH))
            error += "Chú ý " + Constant.MIN_LENGTH + " < Mật Khẩu < " + Constant.MAX_LENGTH;
        return error;
    }

    public static void main(String[] args) {
        try {
            int rule = checkLogin("admin", "12345");
            System.out.println(rule);
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
//						e.printStackTrace();
        }
//		System.out.println(encrypt("12345", "sdfg"));
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
            if (checkEmpty(userInfor.getFullname())) {
                noteError += "Hãy nhập họ tên";
            } else if (userInfor.getAge() <= 0) {
                noteError += "Hãy nhập tuổi";
            } else if (checkEmpty(userInfor.getDiseaseName())) {
                noteError += "Hãy nhập tên bệnh";
            } else if (checkEmpty(userInfor.getTel())) {
                noteError += "Hãy nhập số điện thoại";
            } else if (checkEmpty(userInfor.getRoom())) {
                noteError += "Hãy chọn phòng";
            } else if (checkLength(userInfor.getFullname(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += "Chú ý " + Constant.MIN_LENGTH + " < Độ dài Họ tên < " + Constant.MAX_LENGTH;
            } else if (checkMinAge(userInfor.getAge(), Constant.MIN_AGE)) {
                noteError += "Chú ý " + "Tuổi phải > " + Constant.MIN_AGE;
            } else if (checkLength(userInfor.getDiseaseName(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += "Chú ý " + Constant.MIN_LENGTH + " < Độ dài Tên bệnh < " + Constant.MAX_LENGTH;
            }
            // check room?
        } else if (Constant.MODE_ADD == mode) {
            if (checkEmpty(userInfor.getFullname())) {
                noteError += "Hãy nhập họ tên";
            } else if (checkEmpty(userInfor.getUsername())) {
                noteError += "Hãy nhập tên đăng nhập";
            } else if (checkEmpty(userInfor.getPassword())) {
                noteError += "Hãy nhập mật khẩu";
            } else if (userInfor.getAge() <= 0) {
                noteError += "Hãy nhập tuổi";
            } else if (checkEmpty(userInfor.getDiseaseName())) {
                noteError += "Hãy nhập tên bệnh";
            } else if (checkEmpty(userInfor.getTel())) {
                noteError += "Hãy nhập số điện thoại";
            } else if (checkEmpty(userInfor.getRoom())) {
                noteError += "Hãy chọn phòng";
            } else if (checkLength(userInfor.getFullname(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += "Chú ý " + Constant.MIN_LENGTH + " < Độ dài Họ tên < " + Constant.MAX_LENGTH;
            } else if (checkLength(userInfor.getUsername(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += "Chú ý " + Constant.MIN_LENGTH + " < Độ dài Tên đăng nhập < " + Constant.MAX_LENGTH;
            } else if (checkLength(userInfor.getPassword(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += "Chú ý " + Constant.MIN_LENGTH + " < Độ dài Mật khấu < " + Constant.MAX_LENGTH;
            } else if (checkMinAge(userInfor.getAge(), Constant.MIN_AGE)) {
                noteError += "Chú ý " + "Tuổi phải > " + Constant.MIN_AGE;
            } else if (checkLength(userInfor.getDiseaseName(), Constant.MIN_LENGTH, Constant.MAX_LENGTH)) {
                noteError += "Chú ý " + Constant.MIN_LENGTH + " < Độ dài Tên bệnh < " + Constant.MAX_LENGTH;
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
}
