<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"]== "GET"){
        $response = array();
        $response["list_news"] = array();
        $query = "SELECT * FROM tbl_news ORDER BY new_id DESC";
        $res_outer = mysqli_query($mysqli,$query);
        $temp_array = array();
        $total_records = mysqli_num_rows($res_outer);
        if ($total_records >= 1) {
            while ($row = mysqli_fetch_assoc($res_outer)) {
                $temp_array["new_id"] = $row["new_id"];
                $temp_array["new_title"] = $row["new_title"];
                $temp_array["new_content"] = $row["new_content"];
                array_push($response["list_news"], $temp_array);
            } 
        }  
        // Thiết lập header là JSON
        header('Content-Type: application/json');
        // Hiển thị kết quả
        echo json_encode($response); 
    }else if ($_SERVER["REQUEST_METHOD"]== "POST") {
        $response = array();
        if (isset($_POST["action"]) == "add"){
            if (isset($_POST["new_title"])) {
                $new_title = htmlspecialchars($_POST["new_title"],ENT_QUOTES);
                $new_content = htmlspecialchars($_POST["new_content"],ENT_QUOTES);
                $query = "INSERT INTO tbl_news(new_title, new_content) VALUES ('$new_title','$new_content')";
                $res_outer = mysqli_query($mysqli,$query);
                if ($res_outer) {
                    // Thêm dữ liệu thành công
                    $response["success"] = 1;
                    $response["message"] = "Thêm thành công.";
                    // Trả kết quả cho client
                     echo json_encode($response);
                } else {
                    // Thêm thất bại
                    $response["success"] = 0;
                    $response["message"] = "Thêm thất bại.";
                    // Trả về kết quả
                    echo json_encode($response);
                }
            }
        }else if (isset($_POST["action"]) == "delete") {
            if (isset($_POST["new_id"])) {
                $new_id = (int)htmlspecialchars($_POST["new_id"],ENT_QUOTES);
                $query = "DELETE FROM tbl_news WHERE new_id = '$user_id'";
                $res_outer = mysqli_query($mysqli,$query);
                if ($res_outer) {
                    // Thêm dữ liệu thành công
                    $response["success"] = 1;
                    $response["message"] = "Thành công.";
                    // Trả kết quả cho client
                     echo json_encode($response);
                } else {
                    // Thêm thất bại
                    $response["success"] = 0;
                    $response["message"] = "Thất bại.";
                    // Trả về kết quả
                    echo json_encode($response);
                }
            }
        }
    }
end:
?>
