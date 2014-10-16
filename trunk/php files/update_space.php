<?php
 
/*
 * Following code will update a parking space information
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['pid']) && isset($_POST['empty'])) {
 
    $pid = $_POST['pid'];
    $empty = $_POST['empty'];
     
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE parking_monitor SET empty = '$empty' WHERE pid = $pid");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Product successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>