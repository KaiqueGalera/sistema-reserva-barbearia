package br.ifsp.demo.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AgendamentoRequestBuilder {

    public static final UUID SERVICO_CORTE = UUID.fromString("11111111-1111-1111-1111-111111111111");

    private UUID barbeiroId = UUID.randomUUID();
    private String nomeDoContato = "Kaique Galera";
    private String telefoneDoContato = "16999998888";
    private LocalDateTime inicio = amanhaAs(10);
    private List<UUID> servicos = List.of(SERVICO_CORTE);

    public static AgendamentoRequestBuilder umAgendamento() {
        return new AgendamentoRequestBuilder();
    }

    public static LocalDateTime amanhaAs(int hora) {
        return LocalDateTime.now().plusDays(1).withHour(hora).withMinute(0).withSecond(0).withNano(0);
    }

    public static LocalDateTime ontemAs(int hora) {
        return amanhaAs(hora).minusDays(2);
    }

    public static String formatar(LocalDateTime data) {
        return data.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public AgendamentoRequestBuilder comBarbeiro(UUID barbeiroId) {
        this.barbeiroId = barbeiroId;
        return this;
    }

    public AgendamentoRequestBuilder comContato(String nome, String telefone) {
        this.nomeDoContato = nome;
        this.telefoneDoContato = telefone;
        return this;
    }

    public AgendamentoRequestBuilder comInicio(LocalDateTime inicio) {
        this.inicio = inicio;
        return this;
    }

    public AgendamentoRequestBuilder comServicos(UUID... servicos) {
        this.servicos = List.of(servicos);
        return this;
    }

    public AgendamentoRequestBuilder semInicio() {
        this.inicio = null;
        return this;
    }

    public AgendamentoRequestBuilder semServicos() {
        this.servicos = null;
        return this;
    }

    public Map<String, Object> construir() {
        Map<String, Object> corpo = new HashMap<>();
        corpo.put("barbeiroId", barbeiroId);
        corpo.put("nomeDoContato", nomeDoContato);
        corpo.put("telefoneDoContato", telefoneDoContato);
        corpo.put("inicio", inicio == null ? null : formatar(inicio));
        corpo.put("servicos", servicos);
        return corpo;
    }
}
