package com.imraniman.projet_android;

import java.io.Serializable;

public class Note implements Serializable {

    private String matiere;
    private double note;

    public Note(String matiere, double note) {
        this.matiere = matiere;
        this.note = note;
    }

    public String getMatiere() {
        return matiere;
    }

    public double getNote() {
        return note;
    }
}

