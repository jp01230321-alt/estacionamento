package model;

import java.time.LocalDateTime;


public class Veiculo {

    private final String placa;
    private final String modelo;
    private final LocalDateTime entrada;
    private LocalDateTime saida;
    private double valorCobrado;

    public Veiculo(String placa, String modelo, LocalDateTime entrada) {
        this.placa = placa;
        this.modelo = modelo;
        this.entrada = entrada;
    }

    public boolean isEstacionado() {
        return saida == null;
    }

    public void registrarSaida(LocalDateTime saida, double valorCobrado) {
        this.saida = saida;
        this.valorCobrado = valorCobrado;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public LocalDateTime getEntrada() { return entrada; }
    public LocalDateTime getSaida() { return saida; }
    public double getValorCobrado() { return valorCobrado; }
}