<?php
$host="127.0.0.1";
$username="root";
$password="root";
$db_name="db_listeCourses";
$con=mysqli_connect("$host", "$username", "$password",$db_name)or die("cannot connect");
mysqli_set_charset($con, "utf8");
?>
