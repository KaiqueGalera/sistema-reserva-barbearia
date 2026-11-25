package br.ifsp.demo.infrastructure;

import br.ifsp.demo.domain.agendamento.AgendamentoId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AgendamentoRepositoryJdbcBordasTest extends PersistenceTestBase {

    @Test
    @DisplayName("Deve retornar vazio ao buscar um agendamento inexistente")
    void deveRetornarVazioAoBuscarUmAgendamentoInexistente() {
        assertThat(agendamentoRepository.porId(new AgendamentoId(UUID.randomUUID()))).isEmpty();
    }
}
