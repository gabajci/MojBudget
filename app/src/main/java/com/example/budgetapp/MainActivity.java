package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

/**
 * Trieda MainActivity obsahuje premenné s ktorými sa pracuje v každom fragmente.
 * Triede prislúchajúci fragment obsahuje nav_host, čiže pri zapnutí sa spustí fragment_hlavnyzoznam.
 * @author Adam Ratkovský
 * @version 1
 * @date apríl 2021
 */
public class MainActivity extends AppCompatActivity {
    /**
     * @param udaje             ArrayList položiek používateľa
     * @param suma              portfolio používateľa
     */
    private ArrayList<Udaj> udaje = new ArrayList<Udaj>();
    private Float suma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Gets udaje.
     *
     * @return the udaje
     */
    public ArrayList<Udaj> getUdaje() {
        return udaje;
    }

    /**
     * Sets udaje.
     *
     * @param udaje the udaje
     */
    public void setUdaje(ArrayList<Udaj> udaje) {
        this.udaje = udaje;
    }

    /**
     * Gets suma.
     *
     * @return the suma
     */
    public Float getSuma() {
        return suma;
    }

    /**
     * Sets suma.
     *
     * @param suma the suma
     */
    public void setSuma(Float suma) {
        this.suma = suma;
    }
}