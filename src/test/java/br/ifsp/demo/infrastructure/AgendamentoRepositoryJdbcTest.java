package br.ifsp.demo.infrastructure;

import br.ifsp.demo.domain.agendamento.Agendamento;
import br.ifsp.demo.domain.agendamento.StatusAgendamento;
import br.ifsp.demo.domain.comum.BarbeiroId;
import br.ifsp.demo.domain.comum.ClienteId;
import br.ifsp.demo.domain.servico.ServicoId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgendamentoRepositoryJdbcTest extends PersistenceTestBase {

    @Test
    @DisplayName("Deve salvar e recuperar agendamento com seus itens")
    void deveSalvarERecuperarAgendamentoComItens() {
        Agendamento agendamento = criarAgendamento(LocalDateTime.of(2030, 6, 10, 10, 0));

        agendamentoRepository.salvar(agendamento);

        assertThat(agendamentoRepository.porId(agendamento.getId())).isPresent();
        Agendamento recuperado = agendamentoRepository.porId(agendamento.getId()).orElseThrow();
        assertThat(recuperado.getId()).isEqualTo(agendamento.getId());
        assertThat(recuperado.getItens()).hasSize(1);
        assertThat(recuperado.getItens().getFirst().getNomeDoServico()).isEqualTo("Corte");
    }

    @Test
    @DisplayName("Deve ordenar agendamentos do cliente por horário de início")
    void deveOrdenarAgendamentosDoClientePorInicio() {
        Agendamento tarde = criarAgendamento(LocalDateTime.of(2030, 6, 10, 15, 0));
        Agendamento manha = Agendamento.reconstituir(
                new br.ifsp.demo.domain.agendamento.AgendamentoId(UUID.randomUUID()),
                tarde.getClienteId(), tarde.getBarbeiroId(), tarde.getContato(),
                new br.ifsp.demo.domain.comum.Periodo(LocalDateTime.of(2030, 6, 10, 9, 0),
                        LocalDateTime.of(2030, 6, 10, 9, 30)),
                java.util.List.of(new br.ifsp.demo.domain.agendamento.ItemDeServico(
                        new br.ifsp.demo.domain.agendamento.ItemId(UUID.randomUUID()),
                        new br.ifsp.demo.domain.servico.ServicoId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                        "Barba", new br.ifsp.demo.domain.comum.Dinheiro(new java.math.BigDecimal("25.00")), 20)),
                StatusAgendamento.AGENDADO, 0, null);

        agendamentoRepository.salvar(tarde);
        agendamentoRepository.salvar(manha);

        assertThat(agendamentoRepository.porCliente(tarde.getClienteId()))
                .extracting(agendamento -> agendamento.getPeriodo().inicio().toLocalTime().toString())
                .containsExactly("09:00", "15:00");
    }

    @Test
    @DisplayName("Deve filtrar por barbeiro, data e status")
    void deveFiltrarPorBarbeiroDataEStatus() {
        Agendamento agendamento = criarAgendamento(LocalDateTime.of(2030, 6, 10, 10, 0));
        agendamentoRepository.salvar(agendamento);

        assertThat(agendamentoRepository.porBarbeiroEData(
                agendamento.getBarbeiroId(), LocalDate.of(2030, 6, 10))).hasSize(1);
        assertThat(agendamentoRepository.porBarbeiroEData(
                new BarbeiroId(UUID.randomUUID()), LocalDate.of(2030, 6, 10))).isEmpty();
        assertThat(agendamentoRepository.porStatus(StatusAgendamento.AGENDADO)).hasSize(1);
        assertThat(agendamentoRepository.porStatus(StatusAgendamento.CANCELADO)).isEmpty();
    }
}
