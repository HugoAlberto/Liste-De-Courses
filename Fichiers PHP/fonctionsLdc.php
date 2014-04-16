<?php
/**
 * ListeDeCourses Function File.
 *
 * Fonctions used by the Android App ListeDeCourses
 *
 * @package 	ListeDeCourses
 * @license 	http://opensource.org/licenses/gpl-license.php  GNU Public License
 * @author 	Hugo Alberto <alberto.hugo05@gmail.com>
 * @link 	http://alberto-hugo.com/
 * @version 	1.0
 */
namespace ListeDeCourses;
include("commun.php");

/**
 * Login
 *
 * @param string $email User's E-mail
 * @param string $password User's password
 * @return array with success, user id, name and email
 */
function login_function($email,$password) {
	global $con;
	// request user with the same email and password
	$result = mysqli_query($con,"SELECT id, nom, email, pass FROM membre WHERE email = '".$email."' AND pass = password('".$password."')") or die(mysql_error());
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber == 1) {
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
 * @param string $name User's name
 * @param string $email User's E-mail
 * @param string $password User's Password
 * @return array with success, user id, name and email
 */
function register_function($name, $email, $password) {
	global $con;
	// request user with the same email
	$result = mysqli_query($con,"SELECT email from membre WHERE email = '".$email."'");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber == 1) {
		// user exist 
		$response["error"] = 2;
		$response["error_msg"] = "User already exist";
		return json_encode($response);		
	} else {
		// user don't exist
		$result = mysqli_query($con,"INSERT INTO membre(id, nom, email, pass) VALUES(NULL, '".$name."', '".$email."',password('".$password."'))");
		if ($result) {
			// user stored
			$uid = mysqli_insert_id($con); // last inserted id
			// get user details 
			$result = mysqli_query($con,"SELECT * FROM membre WHERE id = $uid");
			$user = mysqli_fetch_array($result);
			$response["success"] = 1;
			$response["uid"] = $user["unique_id"];
			$response["user"]["name"] = $user["name"];
			$response["user"]["email"] = $user["email"];
			return json_encode($response);
		} else {
			// user not stored
			$response["error"] = 1;
			$response["error_msg"] = "Error occured in Registartion";
			return json_encode($response);
		}
	}
}

/**
 * New password
 *
 * @param string $password New password
 * @param integer $userId App User's Id
 * @return success or error
 */
function newPassword_function($password, $userId) {
	global $con;
	// request a password update
	$result = mysqli_query($con,"UPDATE membre SET pass = password('".$password."') WHERE id = $userId;");
	if($result) {
		// password changed successfully
		$response["success"] = 2;
		return json_encode($response);
	} else {
		// error changing the password
		$response["error"] = 2;
		return json_encode($response);
	}
}

/**
 * List share
 *
 * @param string $email E-mail you want to share your list with
 * @param integer $userId App User's Id
 * @return success or error
 */
function share_function($email,$userId) {
	global $con;
	// request users with the same email
	$result = mysqli_query($con,"SELECT id FROM membre WHERE email = '".$email."'");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber == 1) {
		// if a user was found
		$row = mysqli_fetch_row($result);
		$userIdToAdd = $row[0];
		// request list id where the owner of the list is the user
		$result = mysqli_query($con,"SELECT id FROM listeNom WHERE owner = $userId");
		while ($row = mysqli_fetch_row($result)) {
			mysqli_query($con,"INSERT INTO partage (membreId, membreQuiPartageId, listeId) VALUES ($userIdToAdd, $userId, $row[0])");
		}
		$response["success"] = 1;
		return json_encode($response);
	} else {
		// if no users was found
		$response["noUserExisting"] = 1;
		return json_encode($response);
	}
}

/**
 * Lists shared with 
 *
 * @param integer $userId User's Id
 * @return array with users mail
 */
function listSharedWith_function($userId) {
	global $con;
	// request email of users you share your list with
	$result=mysqli_query($con,"SELECT email FROM membre INNER JOIN partage ON membreId = id WHERE membreQuiPartageId = $userId");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber > 0) {
		// users were found
		$monTableau = array();
		while($ligne = mysqli_fetch_assoc($result)) {
			$monTableau['membreNom'][] = $ligne;
		}
		return json_encode($monTableau);
	} else {
		// users were not found
		$monTableau['error'] = "You share with no one :(";
		return json_encode($monTableau);
	}
}

/**
 * Delete Shared User
 *
 * @param string $email E-mail you delete
 * @param integer $userId App User's Id
 * @return success or error
 */
function deleteSharedUser_function($email, $userId) {
	global $con;
	// request user id with the same mail
	$idUser = mysqli_query($con,"SELECT id FROM membre WHERE email = '".$email."'");
	$rowNumber = mysqli_num_rows($idUser);
	if ($rowNumber == 1) {
		$row = mysqli_fetch_row($idUser);
		$userIdToDelete = $row[0];
		// request delete with the founded id
		$result = mysqli_query($con,"DELETE FROM partage WHERE membreId = $userIdToDelete AND membreQuiPartageId = $userId");
		if ($result) {
			// delete successful
			$response["success"] = 1;
			return json_encode($response);
		} else {
			// error while deletion
			$response["error"] = 1;
			return json_encode($response);
		}
	} else {
		// no user id found
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Buy Product
 *
 * @param array $productArray Array of product number
 * @param integer $noCurrentList List number
 * @return void
 */
function buyProduct_function($productArray,$noCurrentList) {
	global $con;
	foreach($productArray as $noProduit) {
		// update request on bought products
		mysqli_query($con,"UPDATE liste SET achete = 1 WHERE produitId = $noProduit AND listeId = $noCurrentList");
	}
}

/**
 * Do Shopping List
 *
 * @param integer $noCurrentList Current list number
 * @return products array
 */
function listDoShopping_function($noCurrentList) {
	global $con;
	// request products infos for the current list
	$result = mysqli_query($con,"SELECT produit.produitId AS produitId, produitLib, listeQte, rayon.rayonId AS rayonId, rayonLib FROM produit INNER JOIN rayon ON rayon.rayonId=produit.rayonId INNER JOIN liste ON liste.produitId=produit.produitId WHERE achete=0 AND liste.listeId=$noCurrentList");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber > 0) {
		// products were found
		$response = array();
		while($ligne = mysqli_fetch_assoc($result)) {
			$response['coursesAFaire'][] = $ligne;
		}
		return json_encode($response);
	} else {
		// no product were found
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * AddProductToList
 * 
 * @tags
 * @param integer $noProduct Product number
 * @param integer $quantity Product quantity
 * @param integer $userId User Id of the list
 * @return sucess or error
 */
function addProductToList_function($noProduct,$quantity,$userId) {
	global $con;
	// request list id with the same user's id
	$result = mysqli_query($con,"SELECT id FROM listeNom WHERE owner = $userId");
	$listId = mysqli_fetch_row($result);
	if ($listId) {
		// list were found
		// insert request
		$result = mysqli_query($con,"INSERT INTO liste(listeId,produitId,listeQte) VALUES($listId[0],$noProduct,$quantity)");
		if($result) {
			// successful insert
			$response["success"] = 1;
			return json_encode($response);
		} else {
			// error
			$response["error"] = 1;
			return json_encode($response);
		}
	} else {
		// no list were found
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Product List
 *
 * @param integer $userId User Id
 * @return success or error
 */
function productList_function($userId) {
	global $con;
	// request list id with the same user's id
	$result = mysqli_query($con,"SELECT id FROM listeNom WHERE owner = $userId");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber > 0) {
		$listId = mysqli_fetch_row($result);
		$response = array();
		// request products from user's list
		$result = mysqli_query($con,"SELECT liste.produitId AS produitId ,produitLib,listeQte FROM liste INNER JOIN produit on produit.produitId = liste.produitId WHERE listeId = $listId[0]");
		while($row = mysqli_fetch_assoc($result)) {
			$response['listeDeCourse'][] = $row;
		}
		return json_encode($response);
	} else {
		// error
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Product List from Radius
 *
 * @param string $radiusName Radius' name
 * @return array of products
 */
function productListFromRadius_function($radiusName) {
	global $con;
	// request products from a radius
	$result = mysqli_query($con,"SELECT produitId, produitLib FROM produit WHERE rayonId=(SELECT rayonId FROM rayon WHERE rayonLib='$radiusName')");
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber > 0) {
		// products were found
		$response = array();
		while($row = mysqli_fetch_assoc($result)) {
			$response['produitsDuRayon'][] = $row;
		}
		return json_encode($response); 
	} else {
		// no product were found
		$response["error"] = 1;
		return json_encode($response);
	}
}

/**
 * Radius List
 *
 * @return array of radiuses
 */
function radiusList_function() {
	global $con;
	// request radiuses
	$result = mysqli_query($con,"SELECT rayonId, rayonLib, rayonOrdre FROM rayon ORDER BY rayonOrdre"); 
	$rowNumber = mysqli_num_rows($result);
	if ($rowNumber > 0) {
		// radiuses were found
		$response = array();
		while($ligne = mysqli_fetch_assoc($result)) {
			$response['rayonInfos'][] = $ligne;
		}
		return json_encode($response);
	} else {
		// no radius were found
		$response["error"] = 1;
		return json_encode($response);
	}
}
?>