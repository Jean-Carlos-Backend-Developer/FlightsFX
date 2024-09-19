package com.jeanespin.flightsfx.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Flight {
    private String numVuelo;
    private String destino;
    private LocalDateTime salida;
    private LocalTime duracion;
    public String getNumVuelo() {
        return numVuelo;
    }

    public void setNumVuelo(String numVuelo) {
        this.numVuelo = numVuelo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    public LocalTime getDuracion() {
        return duracion;
    }

    public void setDuracion(LocalTime duracion) {
        this.duracion = duracion;
    }

    //Constructor con número de vuelo
    public Flight(String numVuelo) {
        this.numVuelo = numVuelo;
    }

    //Constructor con todos los atributos
    public Flight(String numVuelo, String destino, LocalDateTime salida, LocalTime duracion) {
        this.numVuelo = numVuelo;
        this.destino = destino;
        this.salida = salida;
        this.duracion = duracion;
    }
}
