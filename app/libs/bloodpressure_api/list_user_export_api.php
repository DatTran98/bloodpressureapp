<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"]== "GET"){
        $response["list_user_export"] = array();
       
            $query = "SELECT u.user_id,u.fullname, u.age, u.disease_name, u.tel,
                    u.username, u.predict_type,u.standard_pressure_max, u.standard_pressure_min,
                    IFNULL(r.room_id,0) as room_id, IFNULL(r.room_name,'') as room_name, IFNULL(s.pressure_max,0) as pressure_max,
                    IFNULL(s.pressure_id,0) as pressure_id,
                    IFNULL(s.pressure_min,0) as pressure_min,
                    IFNULL(s.heart_beat,0) as heart_beat,
                    IFNULL(s.time,'00-00-00') as time 
                  FROM tbl_user u LEFT JOIN tbl_room r ON u.room_id = r.room_id
                        LEFT JOIN tbl_result s ON u.user_id = s.user_id  WHERE u.rule != 0";

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
                $temp_array["username"] = $row["username"];
                $temp_array["predict_type"] = $row["predict_type"];
                $temp_array["standard_max"] = $row["standard_pressure_max"];
                $temp_array["standard_min"] = $row["standard_pressure_min"];
                $temp_array["pressure_id"] = $row["pressure_id"];
                $temp_array["pressure_max"] = $row["pressure_max"];
                $temp_array["pressure_min"] = $row["pressure_min"];
                $temp_array["heart_beat"] = $row["heart_beat"];
                $temp_array["time"] = date('Y-m-d', strtotime(str_replace('-','/', $row["time"])));
                array_push($response["list_user_export"], $temp_array);
            } 
        }  
        // Thiết lập header là JSON
        header('Content-Type: application/json');
        // Hiển thị kết quả
        echo json_encode($response); 
    }
       
end:
?>