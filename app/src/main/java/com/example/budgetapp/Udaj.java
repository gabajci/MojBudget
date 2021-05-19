package com.example.budgetapp;

public class Udaj {
    private String datum;
    private String suma;
    private String poznamka;

    public Udaj(String datum, String suma, String poznamka)
    {
        this.datum=datum;
        this.suma=suma;
        this.poznamka=poznamka;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }
}
