package br.ifsp.demo.infrastructure;

import br.ifsp.demo.domain.servico.ServicoId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogoDeServicosJdbcTest extends PersistenceTestBase {

    @Test
    @DisplayName("Deve buscar serviço existente pelo identificador")
    void deveBuscarServicoExistente() {
        ServicoId id = new ServicoId(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        assertThat(catalogoDeServicos.porId(id)).isPresent();
        assertThat(catalogoDeServicos.porId(id).orElseThrow().nome()).isEqualTo("Corte");
    }

    @Test
    @DisplayName("Deve retornar vazio para serviço inexistente")
    void deveRetornarVazioParaServicoInexistente() {
        assertThat(catalogoDeServicos.porId(new ServicoId(UUID.randomUUID()))).isEmpty();
    }
    @Test
    @DisplayName("Deve carregar os três serviços padrões do catálogo")
    void deveCarregarOsTresServicosPadroes() {
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM servico", Integer.class)).isEqualTo(3);
        assertThat(catalogoDeServicos.porId(new ServicoId(UUID.fromString("11111111-1111-1111-1111-111111111111"))).orElseThrow().nome()).isEqualTo("Corte");
        assertThat(catalogoDeServicos.porId(new ServicoId(UUID.fromString("22222222-2222-2222-2222-222222222222"))).orElseThrow().nome()).isEqualTo("Barba");
        assertThat(catalogoDeServicos.porId(new ServicoId(UUID.fromString("33333333-3333-3333-3333-333333333333"))).orElseThrow().nome()).isEqualTo("Sobrancelha");
    }
}
