package com.example.budgetapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Trieda UdajListAdapter slúži ako adapter pre vykreslenie údajov
 * v požadovanom layoute-TextViewi
 * @author -
 * @version 1
 * @date apríl 2021
 */
public class UdajListAdapter extends ArrayAdapter<Udaj> {
    /**
     * @param TAG           pomocný TAG
     * @param mContext      pomocný kontext
     * @param mResource     pomocná premenná
     */
    private static final String TAG ="UdajListAdapter";
    private Context mContext;

    int mResource;

    /**
     * Inicializuje UdajListAdapter
     *
     * @param context  the context
     * @param resource the resource
     * @param objects  the objects
     */
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

        TextView Tdatum =(TextView)convertView.findViewById(R.id.textDatum);
        TextView Tsuma =(TextView)convertView.findViewById(R.id.textSumaA);
        TextView Tpozn =(TextView)convertView.findViewById(R.id.textPoznamkaA);

        float sumaFloat= Float.parseFloat(suma);

        if(sumaFloat>0)
        {
            Tsuma.setBackgroundColor(0xFFB5E3AB);
            Tdatum.setBackgroundColor(0xFFB5E3AB);
            Tpozn.setBackgroundColor(0xFFB5E3AB);
        } else if(sumaFloat<0)
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
