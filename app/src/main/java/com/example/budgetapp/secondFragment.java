package com.example.budgetapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.budgetapp.MainFragment.PREFS_NAME;

public class secondFragment extends Fragment{

    private SharedPreferences udajeUzivatela;
    private SharedPreferences.Editor editujUdaje;
    private ArrayList<Udaj> udaje = new ArrayList<Udaj>();


    private Activity activity;
    private EditText sumaNaPridanie;
    private EditText poznamka;
    private Spinner spin;
    private Button zmena;
    private View view;
    private RadioButton prijem;

    public static secondFragment newInstance(String param1, String param2) {
        secondFragment fragment = new secondFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public secondFragment() {
        // Required empty public constructor
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
        if(id == R.id.item2)
        {
            hideSoftKeyboard(getActivity());
            sumaNaPridanie.setText("");
            poznamka.setText("");
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_mainFragment);

        } else if(id == R.id.item1)
        {
            hideSoftKeyboard(getActivity());
            sumaNaPridanie.setText("");
            poznamka.setText("");
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_nastavenia);
        } else if(id == R.id.item3)
        {
            hideSoftKeyboard(getActivity());
            sumaNaPridanie.setText("");
            poznamka.setText("");
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_statistika);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_second, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        sumaNaPridanie=view.findViewById(R.id.SumaNaPridanie);
        poznamka=view.findViewById(R.id.poznamka);
        zmena=view.findViewById(R.id.zmena);
        prijem = view.findViewById(R.id.radioButton2);
        spin = view.findViewById(R.id.spinner3);
        setSpinner();

        udaje=((MainActivity)getActivity()).getUdaje();


        zmena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonZmena();
            }
        });

        checkNightMode();
        return view;
    }

    private void setSpinner() {
        String[] items = new String[]{
                "Nezaradené","Výplata","Poplatky","Jedlo","Investície","Voľný čas",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        spin.setAdapter(adapter);
    }

    public void checkNightMode()
    {
        udajeUzivatela = this.getActivity().getSharedPreferences(PREFS_NAME,0);
        editujUdaje = udajeUzivatela.edit();
        String akyRezim = "";
        akyRezim = udajeUzivatela.getString("rezim",akyRezim);

        if(akyRezim.equals("n")) {
            view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.nightmode));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
    }

    public void buttonZmena(){
        if(!sumaNaPridanie.getText().toString().isEmpty()) {

            String minus="";
            if(!prijem.isChecked()){
                minus = "-";
            }

            String kategoria = spin.getSelectedItem().toString()+";";
            String poznam = kategoria+poznamka.getText().toString()+"\n";
            String datum = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())+"\n";

            float portfolio = 0;
            portfolio = ((MainActivity)getActivity()).getSuma();

            if(!prijem.isChecked()){
                portfolio -= Float.parseFloat(sumaNaPridanie.getText().toString());
            } else {
                portfolio+= Float.parseFloat(sumaNaPridanie.getText().toString());
            }

            String suma=minus+sumaNaPridanie.getText().toString()+"\n";

            udaje= ((MainActivity)getActivity()).getUdaje();


            saveUdaje(datum,suma,poznam);
            udaje.add(new Udaj(datum,suma,poznam));
            ((MainActivity)getActivity()).setUdaje(this.udaje);

            saveSuma(String.valueOf(portfolio));
            ((MainActivity)getActivity()).setSuma(portfolio);

            editujUdaje.putFloat("portfolio", portfolio);
            sumaNaPridanie.setText("");
            poznamka.setText("");
            Toast.makeText(getActivity(),"Položka úspešne pridaná.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(),"Zadaj sumu!",Toast.LENGTH_SHORT).show();
        }
        hideSoftKeyboard(getActivity());
    }



    public void saveSuma(String suma)
    {
        FileOutputStream zapis = null;
        try {
            zapis = view.getContext().openFileOutput("suma",Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(zapis);
            osw.write(suma);
            osw.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(zapis != null)
            {
                try {
                    zapis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void saveUdaje(String dat,String sum,String po)
    {
        FileOutputStream zapis = null;
        try {
            zapis = view.getContext().openFileOutput("udaje",Context.MODE_APPEND);

            OutputStreamWriter osw = new OutputStreamWriter(zapis);
            osw.write(dat);
            osw.write(sum);
            osw.write(po);
            osw.flush();
            osw.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            if(zapis != null)
            {
                try {
                    zapis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }





    private void hideSoftKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }



}