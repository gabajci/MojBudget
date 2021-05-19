package com.example.budgetapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.budgetapp.MainFragment.PREFS_NAME;


public class statistika extends Fragment {

    private SharedPreferences udajeUzivatela;
    private SharedPreferences.Editor editujUdaje;

    private Spinner spinner;
    private AnyChartView graf;
    private ArrayList<String> itemy = new ArrayList<String>();
    private ArrayList<Udaj> udaje = new ArrayList<Udaj>();

    private Button vykonaj;
    private TextView infoGrafText;

    private RadioGroup prijemVydaj;
    private int prijemVydajInt=1;

    private float[] vstupy = {0,0,0,0,0,0};
    private Pie pie;
    private View view;
    public statistika() {
        // Required empty public constructor
    }


    public static statistika newInstance(String param1, String param2) {
        statistika fragment = new statistika();
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
        if(id == R.id.item2)
        {
            editujUdaje.putString("stav","nic");
            editujUdaje.commit();
            Navigation.findNavController(view).navigate(R.id.action_statistika_to_mainFragment);

        } else if(id == R.id.item1)
        {
            editujUdaje.putString("stav","nic");
            editujUdaje.commit();
            Navigation.findNavController(view).navigate(R.id.action_statistika_to_nastavenia);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statistika, container, false);
        infoGrafText = view.findViewById(R.id.infoGrafText);
        spinner = view.findViewById(R.id.spinner2);
        graf = view.findViewById(R.id.graf);

        pie = AnyChart.pie();
        graf.setChart(pie);



        vykonaj= view.findViewById(R.id.vykonajB);
        vykonaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupGraph();
            }
        });


        prijemVydaj = view.findViewById(R.id.group1);
        prijemVydaj.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = prijemVydaj.findViewById(checkedId);
                int index = prijemVydaj.indexOfChild(radioButton);
                switch (index) {

                    case 0:
                        prijemVydajInt=1;
                        itemy.clear();
                        setSpinner();
                        graf.setVisibility(View.INVISIBLE);
                        //infoGrafText.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        prijemVydajInt=2;
                        itemy.clear();
                        setSpinner();
                        graf.setVisibility(View.INVISIBLE);
                        //infoGrafText.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        graf = view.findViewById(R.id.graf);
        graf.setVisibility(View.INVISIBLE);
        infoGrafText.setVisibility(View.INVISIBLE);
        setSpinner();

        checkNightMode();

        return view;
    }

    public void setupGraph()
    {
        if(prijemVydajInt==1)
        {
            infoGrafText.setText("Príjem - "+spinner.getSelectedItem().toString());
        }
        else {
            infoGrafText.setText("Výdaj - "+spinner.getSelectedItem().toString());
        }
        String[] months = {"Nezaradené","Výplata","Poplatky","Jedlo","Investície","Voľný čas"};
        refreshGraph();
        List<DataEntry> vstup = new ArrayList<>();

        for(int i = 0; i < months.length;i++)
        {
            vstup.add(new ValueDataEntry(months[i],vstupy[i]));
        }
        pie.data(vstup);
        graf.setVisibility(View.VISIBLE);
        infoGrafText.setVisibility(View.VISIBLE);
    }

    private void refreshGraph()
    {
        for(int i = 0; i< 6;i++)
        {
            vstupy[i]=0;
        }
        udajeUzivatela = this.getActivity().getSharedPreferences(PREFS_NAME,0);
        editujUdaje = udajeUzivatela.edit();

        for(int i = 0; i < udaje.size();i++)
        {
            String znamienkoSumy = udaje.get(i).getSuma().substring(0,1);
            if((prijemVydajInt==1 && !znamienkoSumy.equals("-"))
                    || (prijemVydajInt==2 && znamienkoSumy.equals("-")) )
            {
                String datum = udaje.get(i).getDatum().substring(3, 10);
                String datumVIteme = spinner.getSelectedItem().toString();
                if (datum.equals(datumVIteme)) {

                    switch (udaje.get(i).getPoznamka().substring(0, 5)) {
                        case "Nezar":
                            vstupy[0] += Float.parseFloat(udaje.get(i).getSuma());
                            break;
                        case "Výpla":
                            vstupy[1] += Float.parseFloat(udaje.get(i).getSuma());
                            break;
                        case "Popla":
                            vstupy[2] += Float.parseFloat(udaje.get(i).getSuma());
                            break;
                        case "Jedlo":
                            vstupy[3] += Float.parseFloat(udaje.get(i).getSuma());
                            break;
                        case "Inves":
                            vstupy[4] += Float.parseFloat(udaje.get(i).getSuma());
                            break;
                        case "Voľný":
                            vstupy[5] += Float.parseFloat(udaje.get(i).getSuma());
                            break;
                    }
                }
            }

        }
        for(int i = 0; i< vstupy.length;i++)
        {
            if (vstupy[i]<0) {
                vstupy[i] *= -1;
            }
        }
    }


    private void setSpinner() {
        refreshItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemy);
        spinner.setAdapter(adapter);
    }

    public void loadUdajeFromStorage() {
        FileInputStream citac = null;

        try {
            citac = view.getContext().openFileInput("udaje");
            InputStreamReader isr = new InputStreamReader(citac);
            BufferedReader br = new BufferedReader(isr);
            String datum = br.readLine();
            while (datum != null) {
                datum += "\n";
                String suma = br.readLine() + "\n";
                String pozn = br.readLine();
                for (int i = 0; i < pozn.length(); i++) {
                    if (pozn.charAt(i) == ';') {
                        pozn = pozn.substring(0, i) + "\n" + pozn.substring(i + 1, pozn.length());
                        break;
                    }
                }
                Udaj udaj = new Udaj(datum, suma, pozn);
                udaje.add(udaj);
                datum = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (citac != null) {
                try {
                    citac.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void refreshItems()
    {
        loadUdajeFromStorage();
        for(int i = 0; i < udaje.size();i++)
        {
            String znamienkoSumy = udaje.get(i).getSuma().substring(0,1);
            if((prijemVydajInt==1 && !znamienkoSumy.equals("-"))
                    || (prijemVydajInt==2 && znamienkoSumy.equals("-")) )
            {
                String datum = udaje.get(i).getDatum().substring(3, 10);
                boolean pridaj = true;
                for (int k = 0; k < itemy.size(); k++) {
                    String itemDate = itemy.get(k).substring(0, 7);
                    if (datum.contentEquals(itemDate)) {
                        pridaj = false;
                    }
                }
                if (pridaj) {
                    itemy.add(datum);
                }
            }
        }
        if(itemy.size()==0) {
            Toast.makeText(getActivity(),"Nenašli sme žiadne položky.",Toast.LENGTH_SHORT).show();
            vykonaj.setVisibility(View.INVISIBLE);
        }
        else {
            vykonaj.setVisibility(View.VISIBLE);
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
            pie.background().fill("#514E4E");
        }
    }

}