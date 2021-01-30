<?php
    include('config.php');
    $response = array();
    if ($_SERVER["REQUEST_METHOD"]== "POST") {
            if (isset($_POST["user_id"])) {
                $user_id = htmlspecialchars($_POST["user_id"],ENT_QUOTES);
                $full_name = htmlspecialchars($_POST["full_name"],ENT_QUOTES);
                $age = (int)htmlspecialchars($_POST["age"],ENT_QUOTES);
                $disease_name = htmlspecialchars($_POST["disease_name"],ENT_QUOTES);
                $tel = htmlspecialchars($_POST["tel"],ENT_QUOTES);

                $query_check_exist_user_id = "SELECT 1 FROM tbl_user WHERE user_id = '$user_id'";
                
                $res_outer_check_id = mysqli_query($mysqli,$query_check_exist_user_id);
                if ($res_outer_check_id->num_rows == 0) {
                    $response["success"] = -1;
                    $response["message"] = "Người dùng không tồn tại.";
                    // Trả về kết quả
                    echo json_encode($response);
                }else{
                    $query = "UPDATE tbl_user SET fullname = '$full_name', age = '$age', disease_name = '$disease_name', tel = '$tel'
                                WHERE user_id = '$user_id'";
                    $res_outer = mysqli_query($mysqli,$query);
                    if ($res_outer) {
                        $response["success"] = 1;
                        $response["message"] = "Thành công.";
                         // Trả về kết quả
                        echo json_encode($response);
                    }else{
                        $response["success"] = 0;
                        $response["message"] = "Thất bại";
                        // Trả về kết quả
                        echo json_encode($response);
                    }
                }
            }else {
            $response["success"] = 0;
            $response["message"] = "Thất bại.";
            // Trả về kết quả
            echo json_encode($response);
            }
    }else {
        $response["success"] = 0;
        $response["message"] = "Thất bại.";
        // Trả về kết quả
        echo json_encode($response);
    }
end:
?>
