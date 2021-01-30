
<?php
 include('config.php');
 $user_id = 10;
 $user_name = 'tranbadat';
 $query_select = "SELECT username FROM tbl_user WHERE t.user_id != $user_id";
 $res_outer = mysqli_query($mysqli, $query_select);
//  $res_outer = mysqli_fetch_array($user_name_db);
             
    // $row = mysqli_fetch_assoc($res_outer);
    // $user_name_db = $row["username"];
    $total_records = mysqli_num_rows($res_outer);
if ($total_records > 0) {
    $response["success"] = 2;
    $response["message"] = "Đã tồn tại.";
    echo json_encode($response);
}
//  include('simple_html_dom.php');
// $url='https://huyetap.net/huyet-ap-cao/'; // tạo biến url cần lấy
 
// // $lines_array=file($url); // dùng hàm file() lấy dữ liệu theo url
 
// // $lines_string=implode('',$lines_array); // chuyển dữ liệu lấy được kiểu mảng thành một biến string
 
// // echo ($lines_string); // hiển thị dữ liệu
// $html = file_get_html($url);
// $noidung = $html->find('#entry-content',0);
 
?>