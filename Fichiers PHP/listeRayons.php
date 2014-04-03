<?php
include("commun.php");
//requete sql
if(isset($_GET['action'])&& $_GET['action']=='ajout')
{
	$nomDuRayon=$_GET['nomRayon'];
	$req='select ifnull(max(rayonId),0)+1 from rayon';
	$res1=mysqli_query($con,$req);
	$result=mysqli_fetch_row($res1);
	$prochainNumeroRayon=$result[0];

	$sql="insert into rayon(rayonId,rayonLib)values($prochainNumeroRayon,'".$nomDuRayon."')";
	$res=mysqli_query($con,$sql);
}
if(isset($_GET['action'])&& $_GET['action']=='delete')
{
	$tabNoRayon=$_GET['tabNoRayon'];
	foreach($tabNoRayon as $noRayon)
	{
		$sql="SELECT produitId FROM produit WHERE rayonId=$noRayon";
		$res=mysqli_query($con,$sql);
		while($row=mysqli_fetch_row($res))
		{			
			$sql2="DELETE FROM liste WHERE produitId=$row[0]";
			$res2=mysqli_query($con,$sql2);
		}
		$sql="DELETE FROM produit WHERE rayonId=$noRayon";
		$res=mysqli_query($con,$sql);
		$sql="DELETE FROM organisation WHERE rayonId=$noRayon";
		$res=mysqli_query($con,$sql);
		$sql="DELETE FROM rayon WHERE rayonId=$noRayon";
		$res=mysqli_query($con,$sql);
	}
}
if(isset($_GET['action'])&& $_GET['action']=='modify')
{
	$noRayon=$_GET['noRayon'];
	$nomRayon=$_GET['nomRayon'];
	$sql="UPDATE rayon SET rayonLib='".$nomRayon."' WHERE rayonId=$noRayon";
	$res=mysqli_query($con,$sql);
}
$sql = "SELECT * FROM rayon ORDER BY rayonOrdre"; 
//execution
$result = mysqli_query($con,$sql);
//le tableau
$monTableau = array();
if(mysqli_num_rows($result))//s'il y a un resultat
{
	while($ligne=mysqli_fetch_assoc($result))
	{
		$monTableau['rayonInfos'][]=$ligne;
	}
}
echo json_encode($monTableau);
?>
