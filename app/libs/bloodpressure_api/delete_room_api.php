<?php
    include('config.php');
    if ($_SERVER["REQUEST_METHOD"]== "POST") {
        $response = array();
        if (isset($_POST["action"]) == "delete"){
            if (isset($_POST["room_id"])) {
                $room_id =(int)htmlspecialchars($_POST["room_id"],ENT_QUOTES);
                
                $query_check = "SELECT 1 FROM tbl_room WHERE room_id = '$room_id'";
                $res_outer_check = mysqli_query($mysqli,$query_check);
                if ($res_outer_check->num_rows > 0) {
                    $query = "DELETE FROM tbl_room WHERE room_id = '$room_id'";
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
                }else {
                    $response["success"] = -1;
                    // Trả về kết quả
                    echo json_encode($response);
                }
            }
        }
    }
end:
?>
