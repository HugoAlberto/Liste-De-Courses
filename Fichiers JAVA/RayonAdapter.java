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

public class RayonAdapter extends ArrayAdapter<ModelRayon> {
	private final List<ModelRayon> list;
	private final Activity context;
	
	public RayonAdapter(Activity context, List<ModelRayon> list) {
		super(context, R.layout.rayon_layout_checkable, list);
		this.context = context;
		this.list = list;
	}
	
	static class ViewHolder {
		protected TextView textNumero;
		protected TextView textLibelle;
		protected CheckBox checkbox;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.rayon_layout_checkable, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.textNumero = (TextView) view.findViewById(R.id.itemNumero);
			viewHolder.textLibelle = (TextView) view.findViewById(R.id.itemLibelle);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.itemCheckBox);
			
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					ModelRayon element = (ModelRayon) viewHolder.checkbox.getTag();
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
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
		}
}