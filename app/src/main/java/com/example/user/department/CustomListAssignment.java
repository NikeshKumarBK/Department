package com.example.user.department;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 4/15/2016.
 */
public class CustomListAssignment extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListAssignment(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtAssign,txtDate,txtsubCode,txtsubName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.single_row_assign, null);
            holder = new ViewHolder();
            holder.txtAssign = (TextView) convertView.findViewById(R.id.txtAssign);
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtsubCode = (TextView) convertView.findViewById(R.id.txtSubCode);
            holder.txtsubName = (TextView) convertView.findViewById(R.id.txtSubName);
            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtAssign.setText(rowItem.getAssign());
        holder.txtDate.setText(rowItem.getDate());
        holder.txtsubCode.setText(rowItem.getSubCode());
        holder.txtsubName.setText(rowItem.getSubName());
        return convertView;
    }
}