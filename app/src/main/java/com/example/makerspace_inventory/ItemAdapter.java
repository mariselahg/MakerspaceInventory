package com.example.makerspace_inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>{

    Context context;

    public ItemAdapter(Context context, int resource, List<Item> itemList)
    {
        super(context,resource,itemList);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Item item = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cell, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.itemName);
        ImageView iv = (ImageView) convertView.findViewById(R.id.itemImage);

        Glide.with(context)
                .load(item.getImage_url())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(iv);

        tv.setText(item.getItemname());
        //iv.setImageResource(R.drawable.ic_baseline_image_24);       //replace: drawable -> item.getImage()

        return convertView;
    }
}
