package com.example.facedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.facedemo.R;

import java.util.List;

/**
 * Created by shugu on 2018/7/14.
 */

public class Myadapter extends BaseAdapter{
    private Context context;
    private List<FaceBean> faceList;
    private LayoutInflater inflater;
    public Myadapter(Context context, List<FaceBean> faceList) {
        super();
        this.context = context;
        this.faceList = faceList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return faceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return faceList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list, null);
            holder.picture = (ImageView) convertView.findViewById(R.id.item_img);
            holder.id = (TextView) convertView.findViewById(R.id.item_id);
            holder.txt = (TextView) convertView.findViewById(R.id.item_txt);
            holder.sim = (TextView) convertView.findViewById(R.id.item_sim);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.id.setText("NO."+faceList.get(position).getId());
        holder.sim.setText(faceList.get(position).getSim());
        holder.txt.setText(faceList.get(position).getTxt());
        holder.picture.setImageResource(faceList.get(position).getRes());
        return convertView;
    }

    class ViewHolder {
        ImageView picture;
        TextView id;
        TextView txt;
        TextView sim;
    }
}
