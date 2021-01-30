<?php
   include('config.php');
    if ($_SERVER["REQUEST_METHOD"]== "POST") {
        $response = array();
        if (isset($_POST["user_id"])) {
            $user_id = htmlspecialchars($_POST["user_id"],ENT_QUOTES);
            $query_check = "SELECT 1 FROM tbl_user WHERE user_id = '$user_id'";
            $res_outer_check = mysqli_query($mysqli,$query_check);
            
            if ($res_outer_check->num_rows > 0) {
                $query1 = "DELETE FROM tbl_result WHERE 
                        EXISTS (SELECT 1 FROM tbl_user 
                                WHERE tbl_result.user_id = tbl_user.user_id 
                                AND tbl_user.user_id = '$user_id')";
                $query2 = "DELETE FROM tbl_mess WHERE 
                        EXISTS (SELECT 1 FROM tbl_user 
                                WHERE tbl_mess.user_id = tbl_user.user_id 
                                AND tbl_user.user_id = '$user_id')";
                $query3 = "DELETE FROM tbl_user WHERE user_id = '$user_id'";
                try {
                    $mysqli->autocommit(FALSE);
                    // mysqli_autocommit($mysqli, false);
                    $mysqli->begin_transaction();
                    $res_outer = mysqli_query($mysqli,$query1);
                    $res_outer2 = mysqli_query($mysqli,$query2);
                    $res_outer3 = mysqli_query($mysqli,$query3);
                    $mysqli->commit();
                    // Xoa dữ liệu thành công
                    $response["success"] = 1;
                    // Trả kết quả cho client
                    echo json_encode($response);
                } catch (\Throwable $th) {
                    $mysqli->rollBack();
                    // Xoa thất bại
                    $response["success"] = 0;
                        // Trả về kết quả
                    echo json_encode($response);
                }
            }else {
                $response["success"] = -1;
                // Trả về kết quả
                echo json_encode($response);
            }
            
        }else{
            $response["success"] = 0;
            // Trả về kết quả
            echo json_encode($response);
        }
    }
end:
?>
