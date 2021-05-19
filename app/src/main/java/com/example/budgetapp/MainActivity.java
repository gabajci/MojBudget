package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Udaj> udaje = new ArrayList<Udaj>();
    private Float suma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public ArrayList<Udaj> getUdaje() {
        return udaje;
    }

    public void setUdaje(ArrayList<Udaj> udaje) {
        this.udaje = udaje;
    }

    public Float getSuma() {
        return suma;
    }

    public void setSuma(Float suma) {
        this.suma = suma;
    }
}