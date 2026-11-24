package br.ifsp.demo.infrastructure;

import br.ifsp.demo.domain.agendamento.Agendamento;
import br.ifsp.demo.domain.agendamento.AgendamentoId;
import br.ifsp.demo.domain.agendamento.Contato;
import br.ifsp.demo.domain.agendamento.ItemDeServico;
import br.ifsp.demo.domain.agendamento.ItemId;
import br.ifsp.demo.domain.agendamento.StatusAgendamento;
import br.ifsp.demo.domain.comum.BarbeiroId;
import br.ifsp.demo.domain.comum.ClienteId;
import br.ifsp.demo.domain.comum.Dinheiro;
import br.ifsp.demo.domain.comum.Periodo;
import br.ifsp.demo.domain.servico.ServicoId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag("PersistenceTest")
@Tag("IntegrationTest")
@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/persistence-test.db")
public abstract class PersistenceTestBase {

    @Autowired
    protected JdbcTemplate jdbc;

    @Autowired
    protected AgendamentoRepositoryJdbc agendamentoRepository;

    @Autowired
    protected CatalogoDeServicosJdbc catalogoDeServicos;

    @BeforeEach
    void limparAgendamentos() {
        jdbc.execute("PRAGMA foreign_keys = ON");
        jdbc.update("DELETE FROM item_de_servico");
        jdbc.update("DELETE FROM agendamento");
    }

    @AfterEach
    void limparDepoisDoTeste() {
        jdbc.update("DELETE FROM item_de_servico");
        jdbc.update("DELETE FROM agendamento");
    }

    protected Agendamento criarAgendamento(LocalDateTime inicio) {
        AgendamentoId agendamentoId = new AgendamentoId(UUID.randomUUID());
        ClienteId clienteId = new ClienteId(UUID.randomUUID());
        BarbeiroId barbeiroId = new BarbeiroId(UUID.randomUUID());
        ItemDeServico item = new ItemDeServico(
                new ItemId(UUID.randomUUID()),
                new ServicoId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                "Corte",
                new Dinheiro(new BigDecimal("40.00")),
                30);

        return Agendamento.reconstituir(
                agendamentoId,
                clienteId,
                barbeiroId,
                new Contato("Cliente Teste", "11999999999"),
                new Periodo(inicio, inicio.plusMinutes(30)),
                List.of(item),
                StatusAgendamento.AGENDADO,
                0,
                null);
    }
}
