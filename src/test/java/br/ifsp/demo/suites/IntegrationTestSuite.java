package br.ifsp.demo.suites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Testes de integração da API e persistência")
@SelectPackages({"br.ifsp.demo.api", "br.ifsp.demo.infrastructure"})
@IncludeTags("IntegrationTest")
public class IntegrationTestSuite {
}
