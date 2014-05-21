<?php
require_once("fonctionsLdc.php");

if (isset($_GET['tag']) && $_GET['tag'] != '') {

	$tag = $_GET['tag'];
	$response = array("tag" => $tag, "success" => 0, "error" => 0);

	switch ($tag) {
		case 'login':
			// Request type is check Login
			echo login_function($_GET['email'],$_GET['password']);
			break;

		case 'register':
			// Request type is Register
			echo register_function($_GET['name'], $_GET['email'], $_GET['password']);
			break;

		case 'password':
			// request for password change
			// $_GET['from'] is the user's id
			echo newPassword_function($_GET['pass'], $_GET['from']);
			break;
		
		case 'share':
			// request to share a list with a user
			// $_GET['from'] is the user's id
			echo share_function($_GET['email'],$_GET['from']);
			break;
		
		case 'listSharedWith':
			// request users you share your list with
			echo listSharedWith_function($_GET['login']);
			break;

		case 'deleteSharedUser':
			// delete a user you share your list with
			echo deleteSharedUser_function($_GET['delete'], $_GET['from']);
			break;

		case 'buyProduct':
			// buy a product
			buyProduct_function($_GET['tabNoProduit'],0);
			break;

		case 'listDoShopping':
			// request the do shopping list
			echo listDoShopping_function($_GET['listId']);
			break;

		case 'addProductToList':
			// add a product to your list
			echo addProductToList_function($_GET['produitId'],$_GET['qte'],$_GET['id']);
			break;

		case 'deleteProductFromList':
			// delete a product from your list
			deleteProductFromList_function($_GET['tabNoProduit']);
			break;

		case 'productList':
			// request list's products
			echo productList_function($_GET['id']);
			break;

		case 'productListFromShelf':
			// request list's product from a shelf
			echo productListFromShelves_function($_GET['rayon']);
			break;

		case 'shelfList':
			// request shelf list
			echo shelfList_function();
			break;

		case 'getLists':
			// request a list of lists
			echo getLists_function($_GET['userId']);
			break;

		default:
			echo "Invalid Request";
			break;

	}
} else {
	echo "Access Denied";
}
?>