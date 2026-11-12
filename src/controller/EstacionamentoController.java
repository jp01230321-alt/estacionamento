package controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

import model.Estacionamento;
import model.Perfil;
import model.Veiculo;


public class EstacionamentoController {

    private static final DateTimeFormatter ENTRADA_FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter EXIBICAO_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final String REGEX_PLACA = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$"; 
    
    private final Estacionamento estacionamento = new Estacionamento();
    private Perfil perfilAtual;

    public boolean autenticar(Perfil perfil, String senha) {
        if (perfil != null && perfil.senhaCorreta(senha)) {
            perfilAtual = perfil;
            return true;
        }
        return false;
    }

    public void sair() { perfilAtual = null; }
    public Perfil getPerfilAtual() { return perfilAtual; }
    public boolean podeVerRelatorio() { return perfilAtual == Perfil.GERENCIA; }

  

    public void registrarEntrada(String placa, String modelo, String data, String hora) {
        String p = normalizarPlaca(placa);
        validarPlaca(p);
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o modelo do veículo.");
        }
        estacionamento.registrarEntrada(p, modelo.trim(), converter(data, hora));
    }

    public List<Veiculo> pesquisar(String placa) {
        String p = normalizarPlaca(placa);
        if (p.isEmpty()) {
            throw new IllegalArgumentException("Digite a placa (ou parte dela) para pesquisar.");
        }
        return estacionamento.pesquisarPorPlaca(p);
    }

    public List<Veiculo> listarEstacionados() { return estacionamento.listarEstacionados(); }
    public List<Veiculo> listarTodos() { return estacionamento.listarTodos(); }

    /** Calcula permanência e valor sem registrar a saída. */
    public String previaSaida(String placa, String data, String hora) {
        Veiculo v = localizarEstacionado(placa);
        LocalDateTime saida = converter(data, hora);
        validarSaida(v, saida);
        Duration d = estacionamento.calcularPermanencia(v, saida);
        return resumo(v, saida, d, estacionamento.calcularValor(d));
    }

    /** Registra a saída e devolve o resumo da cobrança. */
    public String registrarSaida(String placa, String data, String hora) {
        Veiculo v = localizarEstacionado(placa);
        LocalDateTime saida = converter(data, hora);
        estacionamento.registrarSaida(v.getPlaca(), saida);
        Duration d = estacionamento.calcularPermanencia(v, saida);
        return resumo(v, saida, d, v.getValorCobrado());
    }

    // ---------- Relatório ----------

    public int getQuantidadeEstacionados() { return estacionamento.getQuantidadeEstacionados(); }
    public int getVagasDisponiveis() { return estacionamento.vagasDisponiveis(); }
    public int getCapacidade() { return Estacionamento.CAPACIDADE; }
    public int getTotalAtendimentos() { return estacionamento.getTotalAtendimentos(); }
    public String getFaturamento() { return moeda(estacionamento.getFaturamento()); }
    public String getTarifa() { return moeda(Estacionamento.TARIFA_POR_HORA); }

    // ---------- Apoio à View ----------

    public String dataAtual() { return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
    public String horaAtual() { return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")); }

    public Object[] linhaTabela(Veiculo v) {
        boolean estac = v.isEstacionado();
        LocalDateTime ate = estac ? LocalDateTime.now() : v.getSaida();
        Duration d = estacionamento.calcularPermanencia(v, ate);
        return new Object[] {
            v.getPlaca(),
            v.getModelo(),
            v.getEntrada().format(EXIBICAO_FMT),
            estac ? "—" : v.getSaida().format(EXIBICAO_FMT),
            formatarDuracao(d),
            estac ? "—" : moeda(v.getValorCobrado()),
            estac ? "Estacionado" : "Saiu"
        };
    }

    // ---------- Internos ----------

    private Veiculo localizarEstacionado(String placa) {
        String p = normalizarPlaca(placa);
        validarPlaca(p);
        Veiculo v = estacionamento.buscarEstacionado(p);
        if (v == null) {
            throw new IllegalArgumentException("Nenhum veículo estacionado com a placa " + p + ".");
        }
        return v;
    }

    private void validarSaida(Veiculo v, LocalDateTime saida) {
        if (saida.isBefore(v.getEntrada())) {
            throw new IllegalArgumentException("A saída não pode ser anterior à entrada ("
                    + v.getEntrada().format(EXIBICAO_FMT) + ").");
        }
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase().replace("-", "").replace(" ", "");
    }

    private void validarPlaca(String p) {
        if (!p.matches(REGEX_PLACA)) {
            throw new IllegalArgumentException("Placa inválida. Use o formato ABC1234 ou ABC1D23.");
        }
    }

    private LocalDateTime converter(String data, String hora) {
        try {
            return LocalDateTime.parse(data.trim() + " " + hora.trim(), ENTRADA_FMT);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("Data ou hora inválida. Use dd/MM/aaaa e HH:mm (ex.: 25/11/2026 14:30).");
        }
    }

    private String resumo(Veiculo v, LocalDateTime saida, Duration d, double valor) {
        return "Placa: " + v.getPlaca() + "\n"
             + "Modelo: " + v.getModelo() + "\n"
             + "Entrada: " + v.getEntrada().format(EXIBICAO_FMT) + "\n"
             + "Saída: " + saida.format(EXIBICAO_FMT) + "\n"
             + "Permanência: " + formatarDuracao(d) + "\n"
             + "Tarifa: " + getTarifa() + " por hora ou fração\n"
             + "Valor a pagar: " + moeda(valor);
    }

    private String formatarDuracao(Duration d) {
        long total = d.toMinutes();
        return (total / 60) + "h " + String.format("%02d", total % 60) + "min";
    }

    private String moeda(double valor) {
        return String.format("R$ %.2f", valor);
    }
}