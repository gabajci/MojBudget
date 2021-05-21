package com.example.budgetapp;

import java.io.Serializable;

/**
 * Trieda údaj je vytvorená pre lepšiu prácu s údajmi užívateľa.
 * @author Adam Ratkovský
 * @version 1
 * @date apríl 2021
 */
public class Udaj implements Serializable {
    /**
     * @param datum    dátum
     * @param suma     suma
     * @param poznamka poznámka
     */
    private String datum;
    private String suma;
    private String poznamka;

    /**
     * Inicializuje nový údaj.
     *
     * @param datum    dátum
     * @param suma     suma
     * @param poznamka poznámka
     */
    public Udaj(String datum, String suma, String poznamka)
    {
        this.datum=datum;
        this.suma=suma;
        this.poznamka=poznamka;
    }

    /**
     * Gets datum.
     *
     * @return the datum
     */
    public String getDatum() {
        return datum;
    }

    /**
     * Sets datum.
     *
     * @param datum the datum
     */
    public void setDatum(String datum) {
        this.datum = datum;
    }

    /**
     * Gets suma.
     *
     * @return the suma
     */
    public String getSuma() {
        return suma;
    }

    /**
     * Sets suma.
     *
     * @param suma the suma
     */
    public void setSuma(String suma) {
        this.suma = suma;
    }

    /**
     * Gets poznamka.
     *
     * @return the poznamka
     */
    public String getPoznamka() {
        return poznamka;
    }

    /**
     * Sets poznamka.
     *
     * @param poznamka the poznamka
     */
    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }
}
