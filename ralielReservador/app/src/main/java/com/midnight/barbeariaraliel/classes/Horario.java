package com.midnight.barbeariaraliel.classes;

import androidx.annotation.NonNull;


public class Horario {
    public String horario;
    public String nomeBarbeiro;
    public String telefoneBarbeiro;
    public int idBarbeiro;

    public String getTelefoneBarbeiro() {
        return telefoneBarbeiro;
    }

    public void setTelefoneBarbeiro(String telefoneBarbeiro) {
        this.telefoneBarbeiro = telefoneBarbeiro;
    }


    public Horario(String horario, String nomeBarbeiro, String telefoneBarbeiro, int idBarbeiro) {
        this.horario = horario;
        this.nomeBarbeiro = nomeBarbeiro;
        this.telefoneBarbeiro = telefoneBarbeiro;
        this.idBarbeiro = idBarbeiro;
    }
    public Horario(String horario, String nomeBarbeiro, String telefoneBarbeiro) {
        this.horario = horario;
        this.nomeBarbeiro = nomeBarbeiro;
        this.telefoneBarbeiro = telefoneBarbeiro;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNomeBarbeiro() {
        return nomeBarbeiro;
    }

    public void setNomeBarbeiro(String nomeBarbeiro) {
        this.nomeBarbeiro = nomeBarbeiro;
    }

    public int getIdBarbeiro() {
        return idBarbeiro;
    }

    public void setIdBarbeiro(int idBarbeiro) {
        this.idBarbeiro = idBarbeiro;
    }
}
