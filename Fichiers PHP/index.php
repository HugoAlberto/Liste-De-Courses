<?php
include("commun.php");
if (isset($_GET['tag']) && $_GET['tag'] != '') {
	$tag = $_GET['tag'];
	$response = array("tag" => $tag, "success" => 0, "error" => 0);
	switch ($tag) {
		case 'login':
			// Request type is check Login
			$email = $_GET['email'];
			$password = $_GET['password'];
			// check for user
			$result = mysqli_query($con,"SELECT * FROM membre WHERE email = '$email'") or die(mysql_error());
			// check for result 
			$row = mysqli_num_rows($result);
			if ($row > 0) {
				// user found
				$result = mysqli_fetch_array($result);
				$user = $result;
				$response["success"] = 1;
				$response["uid"] = $user["id"];
				$response["user"]["name"] = $user["nom"];
				$response["user"]["email"] = $user["email"];
				echo json_encode($response);
			} else {
				// user not found
				$response["error"] = 1;
				$response["error_msg"] = "Incorrect email or password!";
				echo json_encode($response);
			}
			break;

		case 'register':
			// Request type is Register new user
			$name = $_GET['name'];
			$email = $_GET['email'];
			$password = $_GET['password'];
			// check if user is already existed
			$result = mysqli_query($con,"SELECT email from membre WHERE email = '$email'");
			$no_of_rows = mysqli_num_rows($result);
			if ($no_of_rows > 0) {
				// user existed 
				$response["error"] = 2;
				$response["error_msg"] = "User already exist";
				echo json_encode($response);		
			} else {
				// user not existed
				$result = mysqli_query($con,"INSERT INTO membre(id, nom, email,pass) VALUES(NULL, '$name', '$email',password('$password'))");
				// check for successful store
				if ($result) {
					// get user details 
					$uid = mysqli_insert_id($con); // last inserted id
					$result = mysqli_query($con,"SELECT * FROM membre WHERE id = $uid");
					$test = "SELECT * FROM membre WHERE id = $uid";
					// return user details
					$user = mysqli_fetch_array($result);
					// user stored successfully
					$response["success"] = 1;
					$response["uid"] = $user["unique_id"];
					$response["user"]["name"] = $user["name"];
					$response["user"]["email"] = $user["email"];
					echo json_encode($response);
				} else {
					// user failed to store
					$response["error"] = 1;
					$response["error_msg"] = "Error occured in Registartion";
					echo json_encode($response);
				}
			}
			break;

		case 'password':
			// request for password change
			$pass = $_GET['pass'];
			$from = $_GET['from'];
			$sql=mysqli_query($con,"UPDATE membre SET pass=password('".$pass."') WHERE id=$from;");
			if($sql) {
				$response["success"] = 2;
				echo json_encode($response);
			}
			break;
		
		case 'share':
			// request to share a list with a user
			// mail of the person we want to share a list with
			$email = $_GET['email'];
			// id of the user using the app
			$from = $_GET['from'];
			$req=mysqli_query($con,"SELECT id FROM membre WHERE email='".$email."'");
			if($req) {
				$row = mysqli_fetch_row($req);
				$lIdUser = $row[0];
				$sql=mysqli_query($con,"SELECT id FROM listeNom WHERE owner=$from");
				while ($row = mysqli_fetch_row($sql)) {
					$sql2 = mysqli_query($con,"INSERT INTO Partage (membreId, membreQuiPartageId, listeId) VALUES ($lIdUser, $from, $row[0])");
				}
				$response["success"] = 1;
				echo json_encode($response);
			}
			else{
				// No user with this email
				$response["noUserExisting"] = 1;
				echo json_encode($response);
			}
			break;
		
		case 'administration':
			// request for share user deletion
			$login = $_GET['login'];
			$sql=mysqli_query($con,"SELECT email FROM membre INNER JOIN Partage ON membreId=id WHERE membreQuiPartageId =$login");
			if(mysqli_num_rows($sql))
			{
				$monTableau = array();
				while($ligne=mysqli_fetch_assoc($sql))
				{
					$monTableau['membreNom'][]=$ligne;
				}
				echo json_encode($monTableau);
			} else {
				// no list found for the user
				$monTableau['error']="You share with no one :(";
				echo json_encode($monTableau);
			}
			break;

		case 'deleteUser':
			// request to share a list with a user
			$email = $_GET['delete'];
			// id of the user using the app
			$from = $_GET['from'];
			$idUser = mysqli_query($con,"SELECT id FROM membre WHERE email='".$email."'");
			$row = mysqli_fetch_row($idUser);
			$lIdUser = $row[0];
			$sql=mysqli_query($con,"DELETE FROM Partage WHERE membreId=$lIdUser AND membreQuiPartageId=$from");
			if ($sql) {
				$response["success"] = 1;
				echo json_encode($response);
			}
		break;
			
		default:
			echo "Invalid Request";
			break;
	}
}else {
	echo "Access Denied";
}
?>