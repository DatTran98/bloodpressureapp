<?php
    include('config.php');
    if ($_SERVER["REQUEST_METHOD"]== "POST") {
        $response = array();
        if (isset($_POST["action"]) == "delete"){
            if (isset($_POST["new_id"])) {
                $new_id =(int)htmlspecialchars($_POST["new_id"],ENT_QUOTES);
                $query = "DELETE FROM tbl_new WHERE new_id = '$new_id'";
                $res_outer = mysqli_query($mysqli,$query);
                if ($res_outer) {
                    // Thêm dữ liệu thành công
                    $response["success"] = 1;
                    $response["message"] = "Xóa thành công.";
                    // Trả kết quả cho client
                     echo json_encode($response);
                } else {
                    // Thêm thất bại
                    $response["success"] = 0;
                    $response["message"] = "Xóa thất bại.";
                    // Trả về kết quả
                    echo json_encode($response);
                }
            }
        }
    }
end:
?>
