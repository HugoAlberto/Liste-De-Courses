<?php
/**
 * FonctionsListeDeCourses
 *
 * @author Hugo Alberto
 * @copyright HugoAlberto
 * @link http://alberto-hugo.com/
 * @see https://github.com/HugoAlberto/Liste-De-Courses
 * @version 1.0
 */

include("commun.php");

/**
 * Login
 *
 * @param email
 * @param password
 * @return array with success, user id, name and email
 */
function login_function($email,$password) {
	global $con;
	$result = mysqli_query($con,"SELECT * FROM membre WHERE email = '$email'") or die(mysql_error());
	$row = mysqli_num_rows($result);
	if ($row > 0) {
		// user found
		$result = mysqli_fetch_array($result);
		$user = $result;
		$response["success"] = 1;
		$response["uid"] = $user["id"];
		$response["user"]["name"] = $user["nom"];
		$response["user"]["email"] = $user["email"];
		return json_encode($response);
	} else {
		// user not found
		$response["error"] = 1;
		$response["error_msg"] = "Incorrect email or password!";
		return json_encode($response);
	}
}

/**
 * Register
 *
 * @param name
 * @param email
 * @param password
 * @return array with success, user id, name and email
 */
function register_function($name, $email, $password) {
	global $con;
	$result = mysqli_query($con,"SELECT email from membre WHERE email = '$email'");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber > 0) {
		// user existed 
		$response["error"] = 2;
		$response["error_msg"] = "User already exist";
		return json_encode($response);		
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
			return json_encode($response);
		} else {
			// user failed to store
			$response["error"] = 1;
			$response["error_msg"] = "Error occured in Registartion";
			return json_encode($response);
		}
	}
}

/**
 * New password
 *
 * @param password
 * @param userId
 * @return success or error
 */
function newPassword_function($password, $userId) {
	global $con;
	$result = mysqli_query($con,"UPDATE membre SET pass = password('".$password."') WHERE id = $userId;");
	if($result) {
		// password changed successfully
		$response["success"] = 2;
		return json_encode($response);
	} else {
		// error in password changing
		$response["error"] = 2;
		return json_encode($response);
	}
}

/**
 * List share
 *
 * @param email
 * @param userId
 * @return success or error
 */
function share_function($email,$userId) {
	global $con;
	$result = mysqli_query($con,"SELECT id FROM membre WHERE email = '".$email."'");
	if($result) {
		$row = mysqli_fetch_row($result);
		$lIdUser = $row[0];
		$result = mysqli_query($con,"SELECT id FROM listeNom WHERE owner = $userId");
		while ($row = mysqli_fetch_row($result)) {
			mysqli_query($con,"INSERT INTO Partage (membreId, membreQuiPartageId, listeId) VALUES ($lIdUser, $userId, $row[0])");
		}
		$response["success"] = 1;
		return json_encode($response);
	} else {
		// No user with this email
		$response["noUserExisting"] = 1;
		return json_encode($response);
	}
}

/**
 * Lists shared with 
 *
 * @param login
 * @return array with users mail
 */
function listSharedWith_function($login) {
	global $con;
	$result=mysqli_query($con,"SELECT email FROM membre INNER JOIN Partage ON membreId = id WHERE membreQuiPartageId = $login");
	if(mysqli_num_rows($result)) {
		$monTableau = array();
		while($ligne = mysqli_fetch_assoc($result)) {
			$monTableau['membreNom'][] = $ligne;
		}
		return json_encode($monTableau);
	} else {
		// no list found for the user
		$monTableau['error'] = "You share with no one :(";
		return json_encode($monTableau);
	}
}

/**
 * Delete Shared User
 *
 * @param email
 * @param userId
 * @return success or error
 */
function deleteSharedUser_function($email, $userId) {
	global $con;
	$idUser = mysqli_query($con,"SELECT id FROM membre WHERE email='".$email."'");
	$row = mysqli_fetch_row($idUser);
	$lIdUser = $row[0];
	$result=mysqli_query($con,"DELETE FROM Partage WHERE membreId=$lIdUser AND membreQuiPartageId=$userId");
	if ($result) {
		$response["success"] = 1;
		return json_encode($response);
	} else {
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Buy Product
 *
 * @param tabNoProduit
 * @param noListeEnCours
 * @return void
 */
function buyProduct_function($tabNoProduit,$noListeEnCours) {
	global $con;
	foreach($tabNoProduit as $noProduit) {
		mysqli_query($con,"UPDATE liste SET achete = 1 WHERE produitId = $noProduit AND listeId = $noListeEnCours");
	}
}

/**
 * Do Shopping List
 *
 * @param noCurrentList
 * @return products array
 */
function listDoShopping_function($noListeEnCours) {
	global $con;
	$sql = "SELECT produit.produitId AS produitId, produitLib, listeQte, rayon.rayonId AS rayonId, rayonLib FROM produit INNER JOIN rayon ON rayon.rayonId=produit.rayonId INNER JOIN liste ON liste.produitId=produit.produitId WHERE achete=0 AND liste.listeId='".$noListeEnCours."'";
	$result = mysqli_query($con,$sql);
	if(mysqli_num_rows($result)) {
		$monTableau = array();
		while($ligne = mysqli_fetch_assoc($result)) {
			$monTableau['coursesAFaire'][] = $ligne;
		}
		return json_encode($monTableau);
	} else {
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * AddProductToList
 * 
 * @param noProduit
 * @param quantity
 * @param ownerId
 * @return sucess or error
 */
function addProductToList_function($noProduit,$qte,$ownId) {
	global $con;
	$result = mysqli_query($con,"SELECT id FROM listeNom WHERE owner = $ownId");
	$listeId = mysqli_fetch_row($result);
	$result = mysqli_query($con,"INSERT INTO liste(listeId,produitId,listeQte) VALUES($listeId[0],$noProduit,$qte)");
	if($result) {
		$response["success"] = 1;
		return json_encode($response);
	} else {
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Product List
 *
 * @param ownerId
 * @return success or error
 */
function productList_function($ownId) {
	global $con;
	$result = mysqli_query($con,"SELECT id FROM listeNom WHERE owner = $ownId");
	if ($result) {
		$listeId = mysqli_fetch_row($result);
		$response = array();
		$result = mysqli_query($con,"SELECT liste.produitId AS produitId ,produitLib,listeQte FROM liste INNER JOIN produit on produit.produitId = liste.produitId WHERE listeId = $listeId[0]");
		while($row = mysqli_fetch_assoc($result)) {
			$response['listeDeCourse'][] = $row;
		}
		return json_encode($response);
	} else {
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Product List from Radius
 *
 * @param radius name
 * @return array of products
 */
function productListFromRadius_function($nomRayon) {
	global $con;
	$sql = "SELECT produitId,produitLib FROM produit WHERE rayonId=(select rayonId from rayon where rayonLib='$nomRayon')"; 
	$result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)) {
		while($row=mysqli_fetch_assoc($result)) {
			$json['produitsDuRayon'][]=$row;
		}
		return json_encode($json); 
	}
}

/**
 * Radius List
 *
 * @return array of radiuses
 */
function radiusList_function() {
	global $con;
	$result = mysqli_query($con,"SELECT * FROM rayon ORDER BY rayonOrdre"); 
	if(mysqli_num_rows($result)) {
		$monTableau = array();
		while($ligne=mysqli_fetch_assoc($result)) {
			$monTableau['rayonInfos'][]=$ligne;
		}
		return json_encode($monTableau);
	}
?>