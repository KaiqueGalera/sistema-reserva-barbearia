package br.ifsp.demo.infrastructure;

import br.ifsp.demo.domain.agendamento.Agendamento;
import br.ifsp.demo.domain.agendamento.AgendamentoId;
import br.ifsp.demo.domain.comum.ClienteId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AgendamentoRepositoryJdbcBordasTest extends PersistenceTestBase {

    @Test
    @DisplayName("Deve retornar vazio ao buscar um agendamento inexistente")
    void deveRetornarVazioAoBuscarUmAgendamentoInexistente() {
        assertThat(agendamentoRepository.porId(new AgendamentoId(UUID.randomUUID()))).isEmpty();
    }

    @Test
    @DisplayName("Não deve listar os agendamentos de outro cliente")
    void naoDeveListarOsAgendamentosDeOutroCliente() {
        Agendamento agendamento = criarAgendamento(LocalDateTime.of(2030, 6, 10, 10, 0));
        agendamentoRepository.salvar(agendamento);

        assertThat(agendamentoRepository.porCliente(agendamento.getClienteId())).hasSize(1);
        assertThat(agendamentoRepository.porCliente(new ClienteId(UUID.randomUUID()))).isEmpty();
    }
}
