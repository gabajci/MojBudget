package com.example.budgetapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.budgetapp.MainFragment.PREFS_NAME;

public class Nastavenia extends Fragment {

    private View view;
    private TextView vymaz;
    private Switch nocnyR;

    private SharedPreferences.Editor editujUdaje;
    private SharedPreferences udajeUzivatela;

    public Nastavenia() {
        // Required empty public constructor
    }

    public static Nastavenia newInstance(String param1, String param2) {
        Nastavenia fragment = new Nastavenia();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_custom,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.itemPolozky)
        {
            Navigation.findNavController(view).navigate(R.id.action_nastavenia_to_mainFragment);

        } else if(id == R.id.itemGraf)
        {
            Navigation.findNavController(view).navigate(R.id.action_nastavenia_to_statistika);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nastavenia, container, false);

        nocnyR = view.findViewById(R.id.switchNightMode);
        vymaz = view.findViewById(R.id.textVymaz);

        nocnyR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.nightmode));
                    editujUdaje.putString("rezim","n");
                    editujUdaje.commit();
                } else
                {
                    view.setBackgroundColor(Color.WHITE);
                    editujUdaje.putString("rezim","d");
                    editujUdaje.commit();
                }
            }
        });

        vymaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRemove();
            }
        });

        checkNightMode();
        return view;
    }

    public void dialogRemove()
    {
        AlertDialog.Builder dial = new AlertDialog.Builder(getActivity());
        dial.setMessage("Naozaj chceš vymazať všetky údaje?").setCancelable(false)
                .setPositiveButton("Áno", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeData();
                        Toast.makeText(getActivity(),"Údaje boli úspešne vymazané.",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = dial.create();
        alert.setTitle("Potvrdenie");
        alert.show();
    }

    public void removeData(){
        ((MainActivity)getActivity()).setUdaje(null);
        ((MainActivity)getActivity()).setSuma(0.0f);

        FileOutputStream zapis = null;
        try {
            zapis = view.getContext().openFileOutput("udaje",Context.MODE_PRIVATE);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }  finally {
            if(zapis != null)
            {
                try {
                    zapis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        FileOutputStream zapis2 = null;
        try {
            zapis2 = view.getContext().openFileOutput("suma",Context.MODE_PRIVATE);
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }  finally {
            if(zapis2 != null)
            {
                try {
                    zapis2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void checkNightMode()
    {
        udajeUzivatela = this.getActivity().getSharedPreferences(PREFS_NAME,0);
        editujUdaje = udajeUzivatela.edit();
        String akyRezim = "";
        akyRezim = udajeUzivatela.getString("rezim",akyRezim);

        if(akyRezim.equals("n")) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.nightmode));
            nocnyR.setChecked(true);
        }
    }
}