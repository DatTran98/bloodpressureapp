<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"]== "GET"){

            $response = array();
            $response["predict"] = array();

            $query = "SELECT predict_type, age FROM tbl_user where rule = 1";
            $res_outer = mysqli_query($mysqli,$query);
             
            $temp_array = array();
            $total_records = mysqli_num_rows($res_outer);
             
            if ($total_records >= 1) {
                while ($row = mysqli_fetch_assoc($res_outer)) {
                    $temp_array["predict_type"] = $row["predict_type"];
                    $temp_array["age"] = $row["age"];
                    array_push($response["predict"], $temp_array);
                } 
            }  
            // Thiết lập header là JSON
                header('Content-Type: application/json');
                // Hiển thị kết quả
                echo json_encode($response); 
    }
end:
?>
