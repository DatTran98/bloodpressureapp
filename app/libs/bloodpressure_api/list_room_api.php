<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"]== "GET"){
        if (isset($_GET["room_id"])) {
            // $room_id = htmlspecialchars($_GET["room_id"],ENT_QUOTES);
            // $room_name =  htmlspecialchars($_GET["room_name"],ENT_QUOTES);
            // $query = "INSERT INTO tbl_room(room_id,room_name) VALUES ('$room_id','$room_name')";
            // $res_outer = mysqli_query($mysqli,$query);
            // echo json_encode($res_outer);
        }else {
            $response = array();
            $response["list_room"] = array();

            $query = "SELECT * FROM tbl_room WHERE room_id != 0";
            $res_outer = mysqli_query($mysqli,$query);
             
            $temp_array = array();
            $total_records = mysqli_num_rows($res_outer);
             
            if ($total_records >= 1) {
                while ($row = mysqli_fetch_assoc($res_outer)) {
                    $temp_array["room_id"] = $row["room_id"];
                    $temp_array["room_name"] = $row["room_name"];
                    array_push($response["list_room"], $temp_array);
                } 
            }  
            // Thiết lập header là JSON
                header('Content-Type: application/json');
                // Hiển thị kết quả
                echo json_encode($response); 
        }
    }else if ($_SERVER["REQUEST_METHOD"]== "POST") {
        $response = array();
        if (isset($_POST["action"]) == "add"){

            if (isset($_POST["room_id"])) {
                $room_id =(int)htmlspecialchars($_POST["room_id"],ENT_QUOTES);
                $room_name =  htmlspecialchars($_POST["room_name"],ENT_QUOTES);
                $query = "INSERT INTO tbl_room(room_id,room_name) VALUES ('$room_id','$room_name')";
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
        }
    }
end:
?>
