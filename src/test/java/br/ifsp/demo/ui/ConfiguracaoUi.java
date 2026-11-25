package br.ifsp.demo.ui;

import com.github.javafaker.Faker;

import java.util.Locale;

public class ConfiguracaoUi {

    public static final String URL_FRONT = System.getProperty("ui.front.url", "http://localhost:3000");
    public static final String URL_API = System.getProperty("ui.api.url", "http://localhost:8081");
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("ui.headless", "false"));
    public static final Faker FAKER = new Faker(Locale.forLanguageTag("pt-BR"));

    private ConfiguracaoUi() {
    }
}
