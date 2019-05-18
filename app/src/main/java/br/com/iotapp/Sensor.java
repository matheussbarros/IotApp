package br.com.iotapp;

public class Sensor {


    private double humidade;
    private double luminosidade;
    private String estado;


    public Sensor() {

    }




    public Sensor(double humidade, double luminosidade, String estado) {
        super();
        this.humidade = humidade;
        this.luminosidade = luminosidade;
        this.estado = estado;
    }




    public double getHumidade() {
        return humidade;
    }




    public void setHumidade(double humidade) {
        this.humidade = humidade;
    }




    public double getLuminosidade() {
        return luminosidade;
    }




    public void setLuminosidade(double luminosidade) {
        this.luminosidade = luminosidade;
    }




    public String getEstado() {
        return estado;
    }




    public void setEstado(String estado) {
        this.estado = estado;
    }




}
