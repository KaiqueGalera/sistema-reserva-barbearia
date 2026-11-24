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
}
