<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"]== "GET"){
        $response["pressure"] = array();
        $response["list_user"] = array();
        if (isset($_GET["user_id"])) {
            $user_id = htmlspecialchars($_GET["user_id"],ENT_QUOTES);
            $query = "SELECT * FROM tbl_user u LEFT JOIN tbl_room r ON u.room_id = r.room_id WHERE user_id = '$user_id'";
            $query_press = "SELECT * FROM tbl_result WHERE user_id = '$user_id' ORDER BY pressure_id DESC LIMIT 1";
        }else {
            $query = "SELECT * FROM tbl_user u LEFT JOIN tbl_room r ON u.room_id = r.room_id ";
        }
        $res_outer = mysqli_query($mysqli, $query);
        $total_records = mysqli_num_rows($res_outer);
        $temp_array = array();
       
        if ($total_records >= 1) {
            while ($row = mysqli_fetch_assoc($res_outer)) {
                $temp_array["user_id"] = $row["user_id"];
                $temp_array["full_name"] = $row["fullname"];
                $temp_array["age"] = $row["age"];
                $temp_array["disease_name"] = $row["disease_name"];
                $temp_array["tel"] = $row["tel"];
                $temp_array["room_id"] = $row["room_id"];
                $temp_array["room_name"] = $row["room_name"];
                $temp_array["rule"] = $row["rule"];
                $temp_array["username"] = $row["username"];
                $temp_array["predict_type"] = $row["predict_type"];
                $temp_array["standard_max"] = $row["standard_pressure_max"];
                $temp_array["standard_min"] = $row["standard_pressure_min"];
                array_push($response["list_user"], $temp_array);
            } 
        }  
        if (isset($_GET["user_id"])) {
            $query_check = "SELECT 1 FROM tbl_result WHERE user_id = '$user_id'";
            $res_outer_check = mysqli_query($mysqli,$query_check);
            if ($res_outer_check) {
                $temp_array_press = array();
                $res_outer_press = mysqli_query($mysqli, $query_press);
                if ($res_outer_press) {
                    $total_records_press = mysqli_num_rows($res_outer_press);
                    if ($total_records_press >= 1) {
                        while ($row = mysqli_fetch_assoc($res_outer_press)){
                            $temp_array_press["pressure_max"] = $row["pressure_max"];
                            $temp_array_press["pressure_min"] = $row["pressure_min"];
                            $temp_array_press["heart_beat"] = $row["heart_beat"];
                            array_push($response["pressure"], $temp_array_press);
                        }
                    }
                }
            }else{
                array_push($response["pressure"], null);
            }
        }
        // Thiết lập header là JSON
            header('Content-Type: application/json');
            // Hiển thị kết quả
            echo json_encode($response); 
    }
end:
?>