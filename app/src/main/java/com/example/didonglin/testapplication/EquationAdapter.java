package com.example.didonglin.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class EquationAdapter extends ArrayAdapter<String> {
	private int resourdId;
	private String equation;
	private View view;
	private ViewHolder viewHolder;
	
	public EquationAdapter(Context context, int viewResourceId, List<String> objects){
		super(context,viewResourceId,objects);
		resourdId = viewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		equation = getItem(position);
		if ( convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourdId, null);
			viewHolder = new ViewHolder();
			viewHolder.equationView = view.findViewById(R.id.equation_item_view);
			view.setTag(viewHolder);	
		}else {
			view = convertView;
			viewHolder = (ViewHolder)view.getTag();	
		}
		viewHolder.equationView.setText(equation);
		return view;
	}
	
	class ViewHolder {
		TextView equationView;
	}
}