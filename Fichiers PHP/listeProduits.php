<?php
include("commun.php");
if(isset($_GET['rayon'])) {
	$nomRayon=$_GET['rayon'];
	$sql = "SELECT produitId,produitLib FROM produit WHERE rayonId=(select rayonId from rayon where rayonLib='$nomRayon')"; 
	$result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)) {
		while($row=mysqli_fetch_assoc($result)) {
			$json['produitsDuRayon'][]=$row;
		}
	}
	echo json_encode($json); 
}
?> 
