<?php
include("commun.php");
//requete sql
$noListeEnCours=0;
if(isset($_GET['action']))
{
	$action=$_GET['action'];
	switch($action)
	{
		case 'acheter': 
			$tabNoProduit=$_GET['tabNoProduit'];
			foreach($tabNoProduit as $noProduit)
			{
				$sql="UPDATE liste SET achete=1 WHERE produitId=$noProduit AND listeId=$noListeEnCours";
				$result = mysqli_query($con,$sql);
			}
		break;
		case 'annuler': 
			$tabNoProduit=$_GET['tabNoProduit'];
			foreach($tabNoProduit as $noProduit)
			{
				$sql="DELETE FROM liste WHERE produitId=$noProduit AND listeId=$noListeEnCours";
				$result = mysqli_query($con,$sql);
			}
		break;
		case 'reporter':
		break;
	}
}
$sql = "SELECT produit.produitId AS produitId, produitLib, listeQte, rayon.rayonId AS rayonId, rayonLib FROM produit INNER JOIN rayon ON rayon.rayonId=produit.rayonId INNER JOIN liste ON liste.produitId=produit.produitId WHERE achete=0 AND liste.listeId='".$noListeEnCours."'";
$result = mysqli_query($con,$sql);
$monTableau = array();
if(mysqli_num_rows($result))//s'il y a un resultat
{
	while($ligne=mysqli_fetch_assoc($result))
	{
		$monTableau['coursesAFaire'][]=$ligne;
	}
}
echo json_encode($monTableau);
?>