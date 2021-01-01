/**
 * Copyright(C) 2020 Hust
 * Constant.java Oct 24, 2020 Trần Bá Đạt
 */
package com.hust.bloddpressure.util;


/**
 * Class chứa các biến hằng của project
 *
 * @author Trần Bá Đạt
 */
public class Constant {

    // Biến quyền ứng dụng
    public static final int ADMIN_RULE = 0;
    public static final int USER_RULE = 1;
    // Biến static cho DB
//	public static final String JDBC_SQL = DatabaseProperties.getValueByKey("JDBC_SQL");
//	public static final String USER_SQL = DatabaseProperties.getValueByKey("USER_SQL");
//	public static final String PASS_SQL = DatabaseProperties.getValueByKey("PASS_SQL");
//	public static final String URL_SQL = DatabaseProperties.getValueByKey("URL_SQL");
    public static final String JDBC_SQL = "com.mysql.jdbc.Driver";
    public static final String USER_SQL = "root";
    public static final String PASS_SQL = "HH1a3110@";
    public static final String URL_SQL = "jdbc:mysql://42.117.189.29:3306/DATN";
    public static final String PROPERTIES_DATABASE_PATH = "classes/database_ja.properties";
    public static final String CONFIG_PATH = "config.properties";

    // Biến static sử dụng cho common
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz"; // a-z
    public static final String ALPHAUPPERCASE = ALPHA.toUpperCase(); // A-Z
    public static final String DIGIST = "0123456789"; // 0-9
    public static final String SPECHAR = "%^/!|;.,$@#&*()?";
    public static final String ALPHA_NUMBERIC = ALPHA + ALPHAUPPERCASE + DIGIST;


    public static final String SHA_1 = "SHA-1";

    public static final int MODE_EDIT = 2;
    public static final int MODE_ADD = 1;
    public static final int MIN_LENGTH = 6;
    public static final int MAX_LENGTH = 20;
    public static final int MIN_AGE = 5;
    public static final int USER_MIN_LENGTH = 4;
    public static final int INT_VALUE_DEFAULT = 0;
    public static final float TEXT_SIZE_MSG = 25;
    public static final String SELECT_ROOM = "Chọn phòng";
    public static final String LABEL_CHART_MAX = "Áp huyết cao";
    public static final int MODE_BASIC = 0;
    public static final int MODE_HISTORY = 1;

    public static final String LABEL_CHART_MIN = "Áp huyết thấp";


    // ACTION
    public static final String ACTION = "action";
    public static final String ACTION_ADD = "add";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_SORT = "sort";
    public static final String ACTION_SEARCH = "search";
    // CONSTANT SET UP FOR URL
    public static final String URL = "http://datn20201.000webhostapp.com/bloodpressure/";
    public static final String URL_LOCAL = "http://10.0.2.2/bloodpressure/";
    public static final String URL_LIST_ROOM = URL_LOCAL + "listroom_api.php";
    public static final String URL_DELETE_ROOM = URL_LOCAL + "deleteroom_api.php";
    public static final String URL_LIST_NEWS = URL_LOCAL + "new_api.php";
    public static final String URL_ADD_ROOM = URL_LOCAL + "listroom_api.php";
    public static final String URL_ADD_NEWS = URL_LOCAL + "new_api.php";
    public static final String URL_LIST_PRESSURE = URL_LOCAL + "pressure_api.php";
    public static final String URL_LIST_USER = URL_LOCAL + "user_api.php";
//    public static final String URL_LIST_ROOM = URL + "listroom_api.php";
//    public static final String URL_DELETE_ROOM = URL + "deleteroom_api.php";
//    public static final String URL_LIST_NEWS = URL + "new_api.php";
//    public static final String URL_ADD_ROOM = URL + "listroom_api.php";
//    public static final String URL_ADD_NEWS = URL + "new_api.php";
//    public static final String URL_LIST_PRESSURE = URL + "pressure_api.php";
//    public static final String URL_LIST_USER = URL + "user_api.php";


    // SET UP FOR DIALOG
    public static final String CONFIRM_TITLE = "Xác nhận";
    public static final String YES = "Đồng ý";
    public static final String NO = "Không đồng ý";
    public static final String DELETED_ROOM = "Đã xóa thông tin phòng ";
    public static final String DELETED_USER = "Đã xóa thông tin bệnh nhân ";
    public static final String CONFIRM_ROOM = "Bạn có chắc chắn xóa phòng này không?";
    public static final String CONFIRM_USER = "Bạn có chắc chắn xóa người dùng này không?";

    public static final String DETAIL = "Chi tiết";
    public static final String MESSAGE_WARING_MAX = "Lần kiểm tra này có nguy cơ cao huyết áp\n Huyết áp tâp thu >=140 hoặc\n Huyết áp tâm tương >=90\n Hãy liên hệ bác sĩ";
    public static final String MESSAGE_WARING_MIN = "Lần kiểm tra này có nguy cơ huyết áp thâp\n Huyết áp tâp thu <=100\n Hãy liên hệ bác sĩ";
    public static final String MESSAGE_NORMAL = "Chỉ số huyết áp bình thường \n Hãy kiểm tra lại thường xuyên";

    // MESSAGE
    public static final String MESAGE_NO_DATA = "Không có dữ liệu";
    public static final String MESSAGE_ADD_SUCCESS = "Tạo thành công";
    public static final String CHOOSE_ROOM = "Chọn phòng";
    public static final String ROOM_CHOOSE_MESS = "Hãy chọn phòng";
    // SET UP FOR JSON
    public static final String LOG_JSON = "JSON Data";
    public static final String MSG_JSON = "Didn't receive any data from server!";
    public static final String MSG_DELETING = "Deleting...";
    public static final String MESSAGE_LOADING = "Loading...";
    public static final String ERROR_TAG = "Error...";

    public static final String JSON_SUCCESS = "success";
    public static final String OBJECT_JSON_LIST_ROOM = "list_room";
    public static final String OBJECT_JSON_LIST_USER = "list_user";
    public static final String OBJECT_JSON_LIST_PRESSURE = "list_pressure";
    public static final String OBJECT_JSON_LIST_NEWS = "list_news";
    public static final String OBJECT_JSON_PRESSURE = "pressure";
    // NAME FOR ENTITY OBJECT ATTRIBUTED
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";
    public static final String USER_ID = "user_id";


    public static final String AGE = "age";
    public static final String FULL_NAME = "full_name";
    public static final String DISEASE_NAME = "disease_name";
    public static final String EMPTY = "";
    public static final String PRESSURE_ID = "pressure_id";
    public static final String PRESSURE_MAX = "pressure_max";
    public static final String PRESSURE_MIN = "pressure_min";
    public static final String TIME = "time";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String RULE = "rule";
    public static final String TEL = "tel";
    public static final String USERNAME = "username";


}

