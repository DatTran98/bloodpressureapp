<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"] == "GET"){
        if (isset($_GET["user_id"])) {
            $response["list_pressure"] = array();
            $user_id = htmlspecialchars($_GET["user_id"],ENT_QUOTES);
            $query_check = "SELECT 1 FROM tbl_result WHERE user_id = '$user_id'";
            // $query = "SELECT * FROM tbl_result WHERE user_id = $user_id ORDER BY pressure_id DESC";
            $query = "SELECT r.pressure_id, r.pressure_max, r.pressure_min, r.heart_beat, r.time,
                        u.fullname, u.standard_pressure_max, u.standard_pressure_min, u.predict_type
                         FROM tbl_result r INNER JOIN tbl_user u 
                         ON r.user_id = u.user_id WHERE r.user_id = '$user_id' ORDER BY r.pressure_id DESC";
            $res_outer_check = mysqli_query($mysqli,$query_check);
            if ($res_outer_check) {
                $res_outer = mysqli_query($mysqli,$query);
                $temp_array = array();
    
                if ($res_outer) {
                    $total_records = mysqli_num_rows($res_outer);
                    while ($row = mysqli_fetch_assoc($res_outer)) {
                        $temp_array["pressure_id"] = $row["pressure_id"];
                        $temp_array["pressure_max"] = $row["pressure_max"];
                        $temp_array["pressure_min"] = $row["pressure_min"];
                        $temp_array["heart_beat"] = $row["heart_beat"];
                        $temp_array["time"] = date('Y-m-d', strtotime(str_replace('-','/', $row["time"])));
                        $temp_array["full_name"] = $row["fullname"];
                        $temp_array["standard_max"] = $row["standard_pressure_max"];
                        $temp_array["standard_min"] = $row["standard_pressure_min"];
                        $temp_array["predict_type"] = $row["predict_type"];
                        array_push($response["list_pressure"], $temp_array);
                    } 
                }  
            }else{
                array_push($response["list_pressure"],null);
            }
            
          // Thiết lập header là JSON
          header('Content-Type: application/json');
          // Hiển thị kết quả
          echo json_encode($response); 
        }
    } 
end:
?>