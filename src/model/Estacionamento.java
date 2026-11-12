package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Estacionamento {

    public static final int CAPACIDADE = 20;
    public static final double TARIFA_POR_HORA = 5.00;

    private final ArrayList<Veiculo> veiculos = new ArrayList<>();
    private double faturamento = 0;

  
    public Veiculo registrarEntrada(String placa, String modelo, LocalDateTime entrada) {
        if (buscarEstacionado(placa) != null) {
            throw new IllegalArgumentException("Já existe um veículo estacionado com a placa " + placa + ".");
        }
        if (vagasDisponiveis() <= 0) {
            throw new IllegalArgumentException("Estacionamento lotado. Não há vagas disponíveis.");
        }
        Veiculo v = new Veiculo(placa, modelo, entrada);
        veiculos.add(v);
        return v;
    }

   
    public Veiculo buscarEstacionado(String placa) {
        for (Veiculo v : veiculos) {
            if (v.isEstacionado() && v.getPlaca().equals(placa)) {
                return v;
            }
        }
        return null;
    }

   
    public ArrayList<Veiculo> pesquisarPorPlaca(String trecho) {
        ArrayList<Veiculo> resultado = new ArrayList<>();
        for (Veiculo v : veiculos) {
            if (v.getPlaca().contains(trecho)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public ArrayList<Veiculo> listarEstacionados() {
        ArrayList<Veiculo> lista = new ArrayList<>();
        for (Veiculo v : veiculos) {
            if (v.isEstacionado()) {
                lista.add(v);
            }
        }
        return lista;
    }

    public ArrayList<Veiculo> listarTodos() {
        return new ArrayList<>(veiculos);
    }


    public Duration calcularPermanencia(Veiculo v, LocalDateTime ate) {
        Duration d = Duration.between(v.getEntrada(), ate);
        return d.isNegative() ? Duration.ZERO : d;
    }

  
    public double calcularValor(Duration permanencia) {
        long minutos = permanencia.toMinutes();
        long horas = (long) Math.ceil(minutos / 60.0);
        if (horas < 1) {
            horas = 1;
        }
        return horas * TARIFA_POR_HORA;
    }

    
    public Veiculo registrarSaida(String placa, LocalDateTime saida) {
        Veiculo v = buscarEstacionado(placa);
        if (v == null) {
            throw new IllegalArgumentException("Nenhum veículo estacionado com a placa " + placa + ".");
        }
        if (saida.isBefore(v.getEntrada())) {
            throw new IllegalArgumentException("A saída não pode ser anterior à entrada.");
        }
        double valor = calcularValor(calcularPermanencia(v, saida));
        v.registrarSaida(saida, valor);
        faturamento += valor;
        return v;
    }

    public int getQuantidadeEstacionados() { return listarEstacionados().size(); }
    public int vagasDisponiveis() { return CAPACIDADE - getQuantidadeEstacionados(); }
    public double getFaturamento() { return faturamento; }
    public int getTotalAtendimentos() { return veiculos.size(); }
}
