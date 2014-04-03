package com.example.listedecourse;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ListeAdapter extends ArrayAdapter<ModelListe> {
	/**
	 * List declaration
	 */
	private final List<ModelListe> list;
	/**
	 * Context declaration
	 */
	private final Activity context;
	
	public ListeAdapter(Activity context, List<ModelListe> list) {
		super(context, R.layout.produit_a_acheter_layout, list);
		this.context = context;
		this.list = list;
	}
	
	static class ViewHolder {
		protected TextView textNumero;
		protected TextView textLibelle;
		protected TextView textQte;
		protected CheckBox checkbox;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.produit_a_acheter_layout, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textNumero = (TextView) view.findViewById(R.id.itemNumeroProduit);
			viewHolder.textLibelle = (TextView) view.findViewById(R.id.itemLibelleProduit);
			viewHolder.textQte = (TextView) view.findViewById(R.id.itemQuantite);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkbox);
			
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					ModelListe element = (ModelListe) viewHolder.checkbox.getTag();
					element.setSelected(buttonView.isChecked());
				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.textNumero.setText(list.get(position).getNo());
		holder.textLibelle.setText(list.get(position).getNom());
		holder.textQte.setText(list.get(position).getNo());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}
}