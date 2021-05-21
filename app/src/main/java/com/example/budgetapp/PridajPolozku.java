package com.example.budgetapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.budgetapp.ZoznamPoloziek.PREFS_NAME;

/**
 * Trieda PridajPolozku pracuje s fragment_pridajpolozku,
 * podľa zadaných parametrov pridáme do listu položku.
 *
 * @author Adam Ratkovský
 * @version 1
 * @date máj 2021
 */
public class PridajPolozku extends Fragment{
    /**
     * @param view                  pohľad na daný fragment
     * @param udajeUzivatela        údaje uživateľa pomocou SharedPreferencies
     * @param editujUdaje           editovanie údajov uživateľa
     * @param editTSumaNaPr         komponent EditText
     * @param editTPoznamka         komponent EditText
     * @param spinnerKategorii      komponent Spinner
     * @param btnPridaj             komponent Button
     * @param radioBtnPrijem        komponent RadioButton
     * @param sumaPredPrevratenim   lokálna premenná ako pomocná pri prevrátení obrazovky
     */

    private View view;

    private SharedPreferences udajeUzivatela;
    private SharedPreferences.Editor editujUdaje;

    private EditText editTSumaNaPr;
    private EditText editTPoznamka;
    private Spinner spinnerKategorii;
    private Button btnPridaj;
    private RadioButton radioBtnPrijem;

    private float sumaPredPrevratenim;

    /**
     * New instance pridaj polozku.
     *
     * @param param1 the param 1
     * @param param2 the param 2
     * @return the pridaj polozku
     */
    public static PridajPolozku newInstance(String param1, String param2) {
        PridajPolozku fragment = new PridajPolozku();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Instantiates a new Pridaj polozku.
     */
    public PridajPolozku() {
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
        if(id == R.id.itemPolozky)
        {
            hideSoftKeyboard(getActivity());
            editTSumaNaPr.setText("");
            editTPoznamka.setText("");
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_mainFragment);

        } else if(id == R.id.itemNastavenia)
        {
            hideSoftKeyboard(getActivity());
            editTSumaNaPr.setText("");
            editTPoznamka.setText("");
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_nastavenia);
        } else if(id == R.id.itemGraf)
        {
            hideSoftKeyboard(getActivity());
            editTSumaNaPr.setText("");
            editTPoznamka.setText("");
            Navigation.findNavController(view).navigate(R.id.action_secondFragment_to_statistika);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).getSuma()!=null) {
            outState.putFloat("suma", ((MainActivity) getActivity()).getSuma());
        } else {
            outState.putFloat("suma", sumaPredPrevratenim);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pridajpolozku, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editTSumaNaPr=view.findViewById(R.id.editTSumaPridanie);
        editTPoznamka=view.findViewById(R.id.editTPozn);
        btnPridaj=view.findViewById(R.id.btnPridaj);
        radioBtnPrijem = view.findViewById(R.id.rbPrijemA);
        spinnerKategorii = view.findViewById(R.id.spinnerKategoria);

        btnPridaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPridaj();
            }
        });

        if(savedInstanceState!= null)
        {
            this.sumaPredPrevratenim=savedInstanceState.getFloat("suma");
        }

        setSpinner();
        checkNightMode();
        return view;
    }

    /**
     * Nastaví kategórie spinnera a pošle mu ich adaptérom.
     */
    private void setSpinner() {
        String[] items = new String[]{
                "Nezaradené","Výplata","Poplatky","Jedlo","Investície","Voľný čas",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        spinnerKategorii.setAdapter(adapter);
    }

    /**
     * Podľa údajov od užívateľa vytvorí inštancie a volá metódy pre zapísanie
     * daných údajov do súboru.
     */
    public void buttonPridaj(){
        if(!editTSumaNaPr.getText().toString().isEmpty()) {
            String minus="";
            if(!radioBtnPrijem.isChecked()){
                minus = "-";
            }

            String kategoria = spinnerKategorii.getSelectedItem().toString()+";";
            String poznam = kategoria+editTPoznamka.getText().toString()+"\n";
            String datum = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())+"\n";

            float portfolio = 0;
            if(((MainActivity)getActivity()).getSuma()!=null) {
                portfolio =((MainActivity)getActivity()).getSuma();
            } else {
                portfolio = sumaPredPrevratenim;
            }

            if(!radioBtnPrijem.isChecked()){
                portfolio -= Float.parseFloat(editTSumaNaPr.getText().toString());
            } else {
                portfolio+= Float.parseFloat(editTSumaNaPr.getText().toString());
            }

            String suma=minus+editTSumaNaPr.getText().toString()+"\n";

            saveUdaje(datum,suma,poznam);
            saveSuma(String.valueOf(portfolio));

            ((MainActivity)getActivity()).setSuma(portfolio);

            editTSumaNaPr.setText("");
            editTPoznamka.setText("");
            Toast.makeText(getActivity(),"Položka úspešne pridaná.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getActivity(),"Zadaj sumu!",Toast.LENGTH_SHORT).show();
        }
        hideSoftKeyboard(getActivity());
    }

    /**
     * Zapíše nové portfólio do súboru.
     *
     * @param suma portfólio
     */
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

    /**
     * Podľa zadaných parametrov pod seba pridá údaje do súboru.
     *
     * @param dat dátum ktorý má zapísať
     * @param sum sumu ktorú má zapísať
     * @param po  poznámku ktorú má zapísať
     */
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

    /**
     * Ak je klávesnica zobrazená, zavrie ju.
     */
    private void hideSoftKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Skontroluje, či je nočný režim, ak áno zmení farbu pozadia.
     */
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