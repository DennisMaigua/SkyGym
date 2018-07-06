<?php
    $con = mysqli_connect("localhost", "id6406724_admin", "admin", "id6406724_skygym");
    
    $first_name = $_POST["first_name"];
    $last_name = $_POST["last_name"];
    $email = $_POST["email"];
    $password = $_POST["password"];
    $statement = mysqli_prepare($con, "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)");
    mysqli_stmt_bind_param($statement, "siss", $first_name, $last_name, $email, $password);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
