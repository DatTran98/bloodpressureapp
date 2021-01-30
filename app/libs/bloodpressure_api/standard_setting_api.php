<?php
    include('config.php');
    $response = array();
    if ($_SERVER["REQUEST_METHOD"]== "POST") {
            if (isset($_POST["user_id"])) {
                $user_id = htmlspecialchars($_POST["user_id"],ENT_QUOTES);
                $standard_pressure_min = (int)htmlspecialchars($_POST["standard_pressure_min"],ENT_QUOTES);
                $standard_pressure_max = (int)htmlspecialchars($_POST["standard_pressure_max"],ENT_QUOTES);

                $query_check_exist_user_id = "SELECT 1 FROM tbl_user WHERE user_id = '$user_id'";
                
                $res_outer_check_id = mysqli_query($mysqli,$query_check_exist_user_id);
                if ($res_outer_check_id->num_rows == 0) {
                    $response["success"] = -1;
                    $response["message"] = "Người dùng không tồn tại.";
                    // Trả về kết quả
                    echo json_encode($response);
                }else{
                    $query = "UPDATE tbl_user SET standard_pressure_min = '$standard_pressure_min', standard_pressure_max = '$standard_pressure_max'
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
        if (isset($_GET["user_id"])) {
            
            $user_id = htmlspecialchars($_GET["user_id"],ENT_QUOTES);

            $query_check_exist_user_id = "SELECT 1 FROM tbl_user WHERE user_id = '$user_id'";
            
            $res_outer_check_id = mysqli_query($mysqli,$query_check_exist_user_id);
            if ($res_outer_check_id->num_rows == 0) {
                $response["success"] = -1;
                $response["message"] = "Người dùng không tồn tại.";
                // Trả về kết quả
                echo json_encode($response);
            }else{
            $response["success"] = 1;
            $response["standard_pressure"] = array();

            $query = "SELECT standard_pressure_max, standard_pressure_min FROM tbl_user WHERE user_id = '$user_id'";
            $res_outer = mysqli_query($mysqli,$query);
             
            $temp_array = array();
            $total_records = mysqli_num_rows($res_outer);
             
            if ($total_records >= 1) {
                while ($row = mysqli_fetch_assoc($res_outer)) {
                    $temp_array["standard_max"] = $row["standard_pressure_max"];
                    $temp_array["standard_min"] = $row["standard_pressure_min"];
                    array_push($response["standard_pressure"], $temp_array);
                } 
            }  
            // Thiết lập header là JSON
                header('Content-Type: application/json');
                // Hiển thị kết quả
                echo json_encode($response); 
            }

        }
    }
end:
?>
