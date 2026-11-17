package br.ifsp.demo.api;

import br.ifsp.demo.security.user.Role;
import br.ifsp.demo.security.user.User;
import com.github.javafaker.Faker;

import java.util.UUID;

public class EntityBuilder {

    private static final Faker faker = Faker.instance();

    public static User criarUsuarioAleatorio(String senhaCriptografada) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(faker.name().firstName())
                .lastname(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(senhaCriptografada)
                .role(Role.USER)
                .build();
    }
}
