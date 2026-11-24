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

    @Test
    @DisplayName("Deve remover itens em cascade ao excluir agendamento")
    void deveRemoverItensEmCascadeAoExcluirAgendamento() {
        Agendamento agendamento = criarAgendamento(LocalDateTime.of(2030, 6, 10, 10, 0));
        agendamentoRepository.salvar(agendamento);

        jdbc.update("DELETE FROM agendamento WHERE id = ?", agendamento.getId().valor().toString());

        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM item_de_servico", Integer.class)).isZero();
    }

    @Test
    @DisplayName("Deve rejeitar NOT NULL ausente em agendamento")
    void deveRejeitarNotNullAusente() {
        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO agendamento (id, cliente_id, barbeiro_id, inicio, fim, status,
                                         quantidade_de_reagendamentos)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """, UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "2030-06-10T10:00", "2030-06-10T10:30",
                "AGENDADO", 0))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("Deve rejeitar chave primária duplicada")
    void deveRejeitarChavePrimariaDuplicada() {
        String id = UUID.randomUUID().toString();
        jdbc.update("""
                INSERT INTO agendamento (id, cliente_id, barbeiro_id, contato_nome, contato_telefone,
                                         inicio, fim, status, quantidade_de_reagendamentos)
                VALUES (?, ?, ?, 'Cliente', '11999999999', '2030-06-10T10:00',
                        '2030-06-10T10:30', 'AGENDADO', 0)
                """, id, UUID.randomUUID().toString(), UUID.randomUUID().toString());

        assertThatThrownBy(() -> jdbc.update("""
                INSERT INTO agendamento (id, cliente_id, barbeiro_id, contato_nome, contato_telefone,
                                         inicio, fim, status, quantidade_de_reagendamentos)
                VALUES (?, ?, ?, 'Outro', '11888888888', '2030-06-10T11:00',
                        '2030-06-10T11:30', 'AGENDADO', 0)
                """, id, UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @DisplayName("Deve contar agendamentos e itens persistidos")
    void deveContarAgendamentosEItensPersistidos() {
        Agendamento agendamento = criarAgendamento(LocalDateTime.of(2030, 6, 10, 10, 0));
        agendamentoRepository.salvar(agendamento);

        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM agendamento", Integer.class)).isEqualTo(1);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM item_de_servico", Integer.class)).isEqualTo(1);
    }
}
