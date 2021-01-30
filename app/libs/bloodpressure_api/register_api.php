<?php
    include('config.php');
    $response = array();
    if ($_SERVER["REQUEST_METHOD"]== "POST") {
            if (isset($_POST["user_id"])) {
                $user_id = htmlspecialchars($_POST["user_id"],ENT_QUOTES);
                $full_name = htmlspecialchars($_POST["full_name"],ENT_QUOTES);
                $username = htmlspecialchars($_POST["username"],ENT_QUOTES);
                $password = htmlspecialchars($_POST["password"],ENT_QUOTES);
                $age = (int)htmlspecialchars($_POST["age"],ENT_QUOTES);
                $disease_name = htmlspecialchars($_POST["disease_name"],ENT_QUOTES);
                $tel = htmlspecialchars($_POST["tel"],ENT_QUOTES);
                $room_id = (int)htmlspecialchars($_POST["room_id"],ENT_QUOTES);
                $salt = htmlspecialchars($_POST["salt"],ENT_QUOTES);
                $predict_type = 0;
                $standard_max = 140;
                $standard_min = 90;
                $rule = 1;
                $password = $password. '' .$salt;
                $password = sha1($password);

                $query_check_exist_user_id = "SELECT 1 FROM tbl_user WHERE user_id = '$user_id'";
                
                $query_check_exist_username = "SELECT 1 FROM tbl_user WHERE username ='$username'";

                $res_outer_check_id = mysqli_query($mysqli,$query_check_exist_user_id);
                if ($res_outer_check_id->num_rows > 0) {
                    $response["success"] = -1;
                    $response["message"] = "Mã người dùng đã tồn tại.";
                    // Trả về kết quả
                    echo json_encode($response);
                }else{
                    $res_outer_check_username = mysqli_query($mysqli,$query_check_exist_username);
                    if ($res_outer_check_username->num_rows > 0) {
                        $response["success"] = -2;
                        $response["message"] = "Tên đăng nhập đã tồn tại.";
                        // Trả về kết quả
                        echo json_encode($response);
                    }else{
                        $query ="INSERT INTO tbl_user (user_id, fullname, age, disease_name, tel, room_id, rule, salt, username, password, predict_type, standard_pressure_max, standard_pressure_min) 
                        VALUES ('$user_id', '$full_name', '$age', '$disease_name', '$tel', '$room_id', '$rule', '$salt', '$username', '$password', '$predict_type', '$standard_max', '$standard_min');";
                        $res_outer = mysqli_query($mysqli,$query);
                        if ($res_outer) {
                            $response["success"] = 1;
                            $response["message"] = "Thêm thành công.";
                             // Trả về kết quả
                            echo json_encode($response);
                        }else{
                            $response["success"] = 0;
                            $response["message"] = "Thêm thất bạii.";
                            // Trả về kết quả
                            echo json_encode($response);
                        }
                        
                    }
                }
            }else {
            $response["success"] = 0;
            $response["message"] = "Thêm thất bại.";
            // Trả về kết quả
            echo json_encode($response);
            }
    }else {
        $response["success"] = 0;
        $response["message"] = "Thêm thất bại.";
        // Trả về kết quả
        echo json_encode($response);
    }
end:
?>
