package com.example.listedecourse;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class AdapterExpandableRayonProduit extends BaseExpandableListAdapter {

	private final Activity context;
	private EnsembleRayons lesRayons;

	public void setEnsRayon(EnsembleRayons lesJolisRayons){
		lesRayons=lesJolisRayons;
	}
	EnsembleRayons getEnsRayon(){
		return lesRayons;
	}
	
	public AdapterExpandableRayonProduit(Activity context) {
		lesRayons = new EnsembleRayons();
		this.context = context;
	}
	
	@Override
	/**
	 * Renvoi le nombre de rayon
	 */
	public int getGroupCount() {
		return lesRayons.getNbRayon();
	}

	/**
	 * Renvoi le nombre d'article du rayon à l'indice groupPosition
	 */
	@Override
	public int getChildrenCount(int groupPosition) {
		ModelRayonGarni leRayonChoisi=lesRayons.getRayon(groupPosition);
		return leRayonChoisi.getNbArticle();
	}

	/**
	 * Renvoi le rayon à l'indice groupPosition
	 */
	@Override
	public Object getGroup(int groupPosition) {
		return lesRayons.getRayon(groupPosition);
	}

	/**
	 * Renvoi l'article 
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return lesRayons.getRayon(groupPosition).getArticle(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return Long.parseLong(lesRayons.getRayon(groupPosition).getNo());
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return Long.parseLong(lesRayons.getRayon(groupPosition).getArticle(childPosition).getNo());
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
	
	static class ViewHolder {
		protected TextView textNumeroR;
		protected TextView textLibelleR;
		protected TextView textNumero;
		protected TextView textLibelle;
		protected TextView textQte;
		protected CheckBox checkbox;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {		
        View v = convertView;
        if (v == null) {
        	LayoutInflater inflator = context.getLayoutInflater();
            v = inflator.inflate(R.layout.produit_layout, null);
        }
        TextView rayonName = (TextView) v.findViewById(R.id.itemLibelleRayon);
        String nomRayon = lesRayons.getRayon(groupPosition).getNom();
        rayonName.setText(nomRayon);
        return v;
	}
	
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.produit_layout_checkable, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textQte = (TextView) view.findViewById(R.id.itemQuantite);
			viewHolder.textNumero = (TextView) view.findViewById(R.id.itemNumeroProduitDsListe);
			viewHolder.textLibelle = (TextView) view.findViewById(R.id.itemLibelleProduitDsListe);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.itemCheckBoxProdDsListe);
			
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					ModelArticle element = (ModelArticle) viewHolder.checkbox.getTag();
					element.setSelected(buttonView.isChecked());
				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(lesRayons.getRayon(groupPosition).getArticle(childPosition));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(lesRayons.getRayon(groupPosition).getArticle(childPosition));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.textNumero.setText(lesRayons.getRayon(groupPosition).getArticle(childPosition).getNo());
		holder.textLibelle.setText(lesRayons.getRayon(groupPosition).getArticle(childPosition).getNom());
		holder.textQte.setText(lesRayons.getRayon(groupPosition).getArticle(childPosition).getQte());
		holder.checkbox.setChecked(lesRayons.getRayon(groupPosition).getArticle(childPosition).isSelected());
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
