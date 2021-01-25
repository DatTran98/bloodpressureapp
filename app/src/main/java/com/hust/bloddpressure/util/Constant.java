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

    // var static rule
    public static final int ADMIN_RULE = 0;
    public static final int USER_RULE = 1;

    // Biến static sử dụng cho common
    public static final String ALPHA = "abcdefghijklmnopqrstuvwxyz"; // a-z
    public static final String ALPHAUPPERCASE = ALPHA.toUpperCase(); // A-Z
    public static final String DIGIST = "0123456789"; // 0-9
    public static final String SPECHAR = "%^/!|;.,$@#&*()?";
    public static final String ALPHA_NUMBERIC = ALPHA + ALPHAUPPERCASE + DIGIST;


    public static final String SHA_1 = "SHA-1";

    public static final int MODE_EDIT = 2;
    public static final int MODE_ADD = 1;
    public static final int MIN_LENGTH = 4;
    public static final int MAX_LENGTH = 20;
    public static final int MIN_AGE = 5;
    public static final int USER_MIN_LENGTH = 4;
    public static final int ID_MAX_LENGTH = 9;
    public static final int INT_VALUE_DEFAULT = 0;
    public static final float TEXT_SIZE_MSG = 25;
    public static final String SELECT_ROOM = "Chọn phòng";
    public static final String LABEL_CHART_MAX = "Huyết áp tâm thu";
    public static final String LABEL_CHART_MIN = "Huyết áp tâm trương";
    public static final int MODE_BASIC = 0;
    public static final int MODE_HISTORY = 1;

    // ACTION
    public static final String ACTION = "action";
    public static final String ACTION_ADD = "add";
    public static final String ACTION_EDIT = "edit";
    public static final String ACTION_DELETE = "delete";
    public static final String ACTION_SORT = "sort";
    public static final String ACTION_SEARCH = "search";
    // CONSTANT SET UP FOR URL
    public static final String URL = "http://datn20201.000webhostapp.com/bloodpressure/";
//    public static final String URL = "http://10.0.2.2:88/bloodpressure/";
//    public static final String URL = "http://10.0.2.2/bloodpressure/";
    public static final String URL_LIST_ROOM = URL + "list_room_api.php";
    public static final String URL_DELETE_ROOM = URL + "delete_room_api.php";
    public static final String URL_LIST_NEWS = URL + "new_api.php";
    public static final String URL_ADD_ROOM = URL + "list_room_api.php";
    public static final String URL_ADD_NEWS = URL + "new_api.php";
    public static final String URL_LIST_PRESSURE = URL + "list_pressure_api.php";
    public static final String URL_LIST_USER = URL + "user_api.php";
    public static final String URL_LOGIN = URL + "login_api.php";
    public static final String URL_REGISTER_USER = URL + "register_api.php";
    public static final String URL_EDIT_USER = URL + "update_user_api.php";
    public static final String URL_DELETE_USER = URL + "delete_user_api.php";
    public static final String URL_LIST_TYPE_PREDICT = URL + "list_type_predict_api.php";
    public static final String URL_LIST_USER_EXPORT = URL + "list_user_export_api.php";

    // SET UP FOR DIALOG
    public static final String CONFIRM_TITLE = "Xác nhận";
    public static final String YES = "Đồng ý";
    public static final String NO = "Không đồng ý";
    public static final String DELETED_ROOM = "Đã xóa thông tin phòng ";
    public static final String DELETED_USER = "Đã xóa thông tin bệnh nhân ";
    public static final String CONFIRM_ROOM = "Bạn có chắc chắn xóa phòng này không?";
    public static final String CONFIRM_USER = "Bạn có chắc chắn xóa người dùng này không?";
    public static final String DETAIL = "Chi tiết";
    public static final String MESSAGE_WARING_MAX = "Lần kiểm tra này có nguy cơ cao huyết áp\n Huyết áp tâp thu lớn hơn hoặc bằng 140 mmHg hoặc\n Huyết áp tâm trương lớn hơn hoặc bằng 90 mmHg\n Hãy liên hệ bác sĩ";
    public static final String MESSAGE_WARING_MIN = "Lần kiểm tra này có nguy cơ huyết áp thâp\n Huyết áp tâp thu nhỏ hơn hoặc bằng 100 mmHg\n Hãy liên hệ bác sĩ";
    public static final String MESSAGE_NORMAL = "Chỉ số huyết áp bình thường \n Hãy kiểm tra lại thường xuyên";
    public static final String MESSAGE_LOGIN_FAILED = "Sai tên đăng nhập hoặc mật khẩu";
    public static final String MESSAGE_EXIST_USERNAME = "Tên đăng nhập đã tồn tại";
    public static final String MESSAGE_SERVER_FAILED = "Lỗi máy chủ, vui lòng quay lại sau";
    // MESSAGE Validate
    public static final String MESAGE_NO_DATA = "Không có dữ liệu";
    public static final String MESSAGE_ADD_SUCCESS = "Đăng ký thành công";
    public static final String MESSAGE_ADD_FAIL = "Đăng ký thất bại.";
    public static final String CHOOSE_ROOM = "Chọn phòng";
    public static final String ROOM_CHOOSE_MESS = "Hãy chọn phòng";
    public static final String MESSAGE_EDIT_SUCCESS = "Chỉnh sửa thành công";
    public static final String MESSAGE_EDIT_FAIL = "Chỉnh sửa thất bại";
    public static final String NOTE = "Chú ý ";
    public static final String EMPTY_MESSAGE = " Hãy nhập ";
    public static final String PASSWORD_NAME = "mật khẩu";
    public static final String USERNAME_NAME = "tên đăng nhập";
    public static final String FULL_NAME_NAME = "họ tên đầy đủ";
    public static final String USER_ID_NAME = "mã bệnh nhân";
    public static final String AGE_NAME = "số tuổi";
    public static final String DISEASE_NAME_NAME = "tên bệnh";
    public static final String TEL_NAME = "số điện thoại";
    public static final String HEART_BEAT_NAME = "Nhịp tim \n";
    public static final String ROOM_NAME_NAME = "/chọn phòng";
    public static final String ROOM_NOT_EXIST = "phòng không tồn tại";

    // SET UP FOR JSON
    public static final String LOG_JSON = "JSON Data";
    public static final String MSG_JSON = "Didn't receive any data from server!";
    public static final String MSG_DELETING = "Deleting...";
    public static final String MESSAGE_LOADING = "Loading...";
    public static final String ERROR_TAG = "Error...";
    public static final String MSG_LOGIN = "Login...";
    public static final String CREATING = "Creating...";
    // PARAM
    public static final String JSON_SUCCESS = "success";
    public static final String OBJECT_JSON_LIST_ROOM = "list_room";
    public static final String OBJECT_JSON_LIST_USER = "list_user";
    public static final String OBJECT_JSON_LIST_PRESSURE = "list_pressure";
    public static final String OBJECT_JSON_LIST_NEWS = "list_news";
    public static final String OBJECT_JSON_PRESSURE = "pressure";
    public static final String OBJECT_USER = "user";
    public static final String OBJECT_JSON_LIST_PREDICT = "predict";
    public static final String OBJECT_JSON_LIST_USER_EXPORT = "list_user_export";
    public static final int SERVER_ERROR = -1;
    public static final int SERVER_SUCCESS = 1;
    public static final int SERVER_FAIL = 0;
    // NAME FOR ENTITY OBJECT ATTRIBUTED
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";
    public static final String USER_ID = "user_id";
    public static final String MESSAGE = "message";

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
    public static final String PASSWORD = "password";
    public static final String PREDICT_TYPE = "predict_type";
    public static final String NEWS_ID = "new_id";
    public static final String NEWS_TITLE = "new_title";
    public static final String NEWS_CONTENT = "new_content";
    public static final int VALUE_NORMAL_PREDICT = 0;
    public static final int VALUE_MAX_PREDICT = 1;
    public static final int VALUE_MIN_PREDICT = 2;
    public static final String FLOW = "flow_from";
    public static final String FROM_NAV = "from_nav";
    public static final String SALT = "salt";
    public static final String HEART_BEAT = "heart_beat";
    public static final String STANDARD_MAX = "standard_max";
    public static final String STANDARD_MIN = "standard_min";
    public static final String OPT_USER = "opt_user";
    public static final String OPT_PRESSURE = "opt_pressure";
    public static final String SYSTOLIC_MAX = "systolic_max";
    public static final String SYSTOLIC_MIN = "systolic_min";
    public static final String DIASTOLIC_MAX = "diastolic_max";
    public static final String DIASTOLIC_MIN = "diastolic_min";

    public static final String PREDICT_NORMAL = "Các kết quả đo cho kết luận huyết áp bình thường";
    public static final String PREDICT_MAX = "Dự đoán bạn có nguy cơ cao huyết áp qua các lần kiểm tra";
    public static final String PREDICT_MIN = "Dự đoán bạn có nguy cơ huyết áp thấp qua các lần kiểm tra";
    public static final String THANKS_READING = "Cám ơn bạn đã đọc tin";
    public static final String PREDICT_CONTENT = "Phỏng đoán:\n Đỏ - Cao huyết áp\n Vàng - Huyết áp thấp\n Xanh - Bình thuòng";
    public static final String PREDICT_MAX_NAME = "Phỏng đoán cao huyết áp";
    public static final String PREDICT_MIN_NAME = "Phỏng đoán huyết áp thấp";
    public static final String PREDICT_NORMAL_NAME = "Bình thường";
    public static final String PREDICT_NAME = "Phỏng đoán tình trạng";
    public static final String PRESS_BACK_AGAIN = "Ấn back 1 lần nữa để thoát";
    public static final String ANALYST_USER = "Thống kê tình trạng bệnh nhân";
    public static final String ANALYST_MAX = "Thống kê tuổi - cao huyết áp";
    public static final String ANALYST_MIN = "Thống kê tuổi - huyết áp thấp";
    public static final String ANALYST_NORMAL = "Thống kê theo tuổi - bình thường";
    public static final String NO_NEWS = "Không có bản tin nào";
    public static final String COMBACK_LATER = "Vui lòng quay lại sau";
    public static final String EDIT_USER_DELETED = "Người dùng không tồn tại hoặc đã bị xóa";
    public static final String MESSAGE_EXIST_ID = "Mã người dùng đã tồn tại";
    public static final String SAVED = "Đã lưu thành công";
    public static final String ERROR_SAVE = "Lưu thất bại";
    public static final String STATIC_NAME = "Phân tích dữ liệu";
    public static final String DEVERLOPING = "Chức năng này đang phát triển";
    public static final String HAVE = "Có ";
    public static final String CHAR = "/";
    public static final String PEOPLE = " người ";
    public static final String NUMBER_GOT = "Chỉ số đo: ";
    public static final String MMHG = " mmHg";
    public static final String AGE_LEVEL = " Độ tuổi ";
    public static final String HEART_BEAT_DIGIT = " nhịp/phút";
    public static final String LABEL_CHART_HEART = "Chỉ số nhịp tim";
    public static final String HELLO = "Xin chào ";


}