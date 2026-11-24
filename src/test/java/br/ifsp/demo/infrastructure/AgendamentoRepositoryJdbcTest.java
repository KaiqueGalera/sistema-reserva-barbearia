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
}
