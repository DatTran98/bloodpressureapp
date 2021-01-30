<?php
    include('config.php');
    if($_SERVER["REQUEST_METHOD"] == "POST"){
        $response["user"] = array();
        $temp_array = array();
        $username = htmlspecialchars($_POST["username"],ENT_QUOTES);
        $password = $_POST["password"];
    
        $query = "SELECT * from tbl_user WHERE username = '$username'";
        $res_outer = mysqli_query($mysqli, $query);
        if ($res_outer) {
            $total_records = mysqli_num_rows($res_outer);
            if ($total_records > 0) {
                $row = mysqli_fetch_assoc($res_outer);
                $password = $password. '' .$row["salt"];
                $password = sha1($password);
                if ($row["password"] == $password) {
                    $temp_array["user_id"] =  $row["user_id"];
                    $temp_array["rule"] =  $row["rule"];	
                    $temp_array["full_name"] =  $row["fullname"];
                    $temp_array["success"] = 1;
                }else {
                    $temp_array["success"] = 0;
                }
        }else{
            $temp_array["success"] = 0;
        }

    }else{
        $temp_array["success"] = 0;
    }
    array_push($response["user"], $temp_array);
    header('Content-Type: application/json');
    // Send result to client
    echo json_encode($response); 
}
?>