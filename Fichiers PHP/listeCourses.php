<?php
include("commun.php");
$get = $_GET['action'];
switch ($get) {
	case 'delete':
		$tabNoProduit=$_GET['tabNoProduit'];
		foreach($tabNoProduit as $noProduit)
		{
			$sql="SELECT produitId FROM produit WHERE rayonId=$noProduit";
			$res=mysqli_query($con,$sql);
			while($row=mysqli_fetch_row($res))
			{			
				$sql2="DELETE FROM liste WHERE produitId=$row[0]";
				$res2=mysqli_query($con,$sql2);
			}
		}
		break;
	
	case 'ajout':
		$noProduit=$_GET['produitId'];
		$qte=$_GET['qte'];
		$ownId=$_GET['id'];
		$sql="SELECT id FROM listeNom WHERE owner=$ownId";
		$result=mysqli_query($con,$sql);
		$listeId=mysqli_fetch_row($result);
		$sql = "INSERT INTO liste(listeId,produitId,listeQte,achete) VALUES($listeId,$noProduit,$qte,0)"; 
		$result = mysqli_query($con,$sql);
		break;

	case 'vue':
		$ownId=$_GET['id'];
		$sql="SELECT id FROM listeNom WHERE owner=$ownId";
		$result=mysqli_query($con,$sql);
		$listeId=mysqli_fetch_row($result);
		$json = array();
		$sql2="SELECT liste.produitId as produitId ,produitLib,listeQte FROM liste INNER JOIN produit on produit.produitId=liste.produitId WHERE listeId =$listeId[0]";
		$result2=mysqli_query($con,$sql2);
		while($row=mysqli_fetch_assoc($result2))
		{
			$json['listeDeCourse'][]=$row;
		}
		echo json_encode($json);
		break;
}
?> 
