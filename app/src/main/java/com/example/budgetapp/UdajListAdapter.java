package com.example.budgetapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UdajListAdapter extends ArrayAdapter<Udaj> {
        private static final String TAG ="UdajListAdapter";
        private Context mContext;
        int mResource;

    public UdajListAdapter(Context context, int resource,ArrayList<Udaj> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        String datum =getItem(position).getDatum();
        String suma =getItem(position).getSuma();
        String poznamka =getItem(position).getPoznamka();

        Udaj udaj = new Udaj(datum,suma,poznamka);

        LayoutInflater inflater =LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView Tdatum =(TextView)convertView.findViewById(R.id.textView);
        TextView Tsuma =(TextView)convertView.findViewById(R.id.textView2);
        TextView Tpozn =(TextView)convertView.findViewById(R.id.textView3);

        float sumaFl= Float.parseFloat(suma);

        if(sumaFl>0)
        {
            Tsuma.setBackgroundColor(0xFFB5E3AB);
            Tdatum.setBackgroundColor(0xFFB5E3AB);
            Tpozn.setBackgroundColor(0xFFB5E3AB);
        } else if(sumaFl<0)
        {
            Tsuma.setBackgroundColor(Color.parseColor("#F5A9A9"));
            Tdatum.setBackgroundColor(Color.parseColor("#F5A9A9"));
            Tpozn.setBackgroundColor(Color.parseColor("#F5A9A9"));
        }

        Tdatum.setText(datum);
        Tsuma.setText(suma);
        Tpozn.setText(poznamka);

        return convertView;
    }
}
