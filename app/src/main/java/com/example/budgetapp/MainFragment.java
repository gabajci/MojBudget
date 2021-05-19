package com.example.budgetapp;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainFragment extends Fragment{

    private View view;
    private Context thisContext;

    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences.Editor editujUdaje;
    private SharedPreferences udajeUzivatela;

    private ArrayList<Udaj> udaje = new ArrayList<Udaj>();

    private TextView textPortfolio;
    private UdajListAdapter adapterListu;
    private ListView listPoloziek;
    private FloatingActionButton floatingBtnPridajPolozku;

    private float portfolio = 0;

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_custom,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.itemGraf)
        {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_statistika);
        } else if(id == R.id.itemNastavenia)
        {

            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_nastavenia);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hlavnyzoznam, container, false);

        thisContext = container.getContext();

        textPortfolio = (TextView) view.findViewById(R.id.textPortfolioSuma);
        floatingBtnPridajPolozku= view.findViewById(R.id.floatbtnPridaj);
        listPoloziek =(ListView) view.findViewById(R.id.listPoloziek);

        floatingBtnPridajPolozku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_secondFragment);
            }
        });

        loadData();
        refreshPortfolioColor();
        checkNightMode();
        return view;
    }

    public void refreshPortfolioColor()
    {
        if(Float.parseFloat(textPortfolio.getText().toString()) >0)
        {
            textPortfolio.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.mygreen));
        }
        else if(Float.parseFloat(textPortfolio.getText().toString())  < 0)
        {
            textPortfolio.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.myred));
        }
        else
        {
            textPortfolio.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.myseda));
        }
    }

    public void loadData()
    {
        loadSumaFromStorage();
        String sumaString = String.valueOf(portfolio );

        if(portfolio  == 0)
        {
            sumaString="0";

        }

        textPortfolio.setText(sumaString);

        loadUdajeFromStorage();

        adapterListu = new UdajListAdapter(thisContext,R.layout.adapter_view_layout,udaje);
        listPoloziek.setAdapter(adapterListu);
    }

    public void loadUdajeFromStorage()
    {
        FileInputStream citac = null;

        try{
            citac= view.getContext().openFileInput("udaje");
            InputStreamReader isr = new InputStreamReader(citac);
            BufferedReader br = new BufferedReader(isr);
            String datum=br.readLine();
            while(datum!= null)
            {
                datum += "\n";
                String suma = br.readLine()+"\n";
                String pozn = br.readLine();
                for(int i = 0; i< pozn.length();i++)
                {
                    if(pozn.charAt(i)==';')
                    {
                        pozn = pozn.substring(0,i)+"\n"+pozn.substring(i+1,pozn.length());
                        break;
                    }
                }
                Udaj udaj = new Udaj(datum,suma,pozn);
                udaje.add(udaj);
                datum = br.readLine();
            }
            br.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(citac != null)
            {
                try {
                    citac.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ((MainActivity)getActivity()).setUdaje(this.udaje);
    }

    public void loadSumaFromStorage()
    {
        FileInputStream citac = null;

        try{
            citac= view.getContext().openFileInput("suma");
            InputStreamReader isr = new InputStreamReader(citac);
            BufferedReader br = new BufferedReader(isr);
            String portfolio=br.readLine();
            if(portfolio== null)
            {
                this.portfolio =0;
            }
            else{
                this.portfolio =Float.parseFloat(portfolio);
            }
            br.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(citac != null)
            {
                try {
                    citac.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ((MainActivity)getActivity()).setSuma(this.portfolio );
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

}