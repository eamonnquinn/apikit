package org.mule.module.apikit;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.tck.junit4.rule.DynamicPort;

import com.jayway.restassured.RestAssured;

import org.junit.Rule;
import org.junit.Test;

public class GeneratedSchemaTestCase extends FunctionalTestCase
{
    @Rule
    public DynamicPort serverPort = new DynamicPort("serverPort");

    @Override
    public int getTestTimeoutSecs()
    {
        return 6000;
    }

    @Override
    protected void doSetUp() throws Exception
    {
        RestAssured.port = serverPort.getNumber();
        super.doSetUp();
    }

    @Override
    protected String getConfigResources()
    {
        return "org/mule/module/apikit/jackson/generated-schema-config.xml";
    }

    @Test
    public void putValidJson() throws Exception
    {
        given().body("{\"username\":\"gbs\",\"firstName\":\"george\",\"lastName\":\"bernard shaw\",\"emailAddresses\":[\"gbs@ie\"]}")
                .contentType("application/json")
            .expect()
                .statusCode(204).body(is(""))
            .when().put("/api/currentuser");
    }

    @Test
    public void putInvalidJson() throws Exception
    {
        given().body("{\"username\":\"gbs\",\"firstName\":\"george\",\"lastName\":\"bernard shaw\"}")
                .contentType("application/json")
            .expect()
                .statusCode(400).body(is("bad request"))
            .when().put("/api/currentuser");
    }

    @Test
    public void putValidXml() throws Exception
    {
        given().body("<user xmlns=\"http://mulesoft.org/schemas/sample\" username=\"gbs\" firstName=\"george\" lastName=\"bernard shaw\">" +
                     "<email-addresses><email-address>gbs@ie</email-address></email-addresses></user>")
                .contentType("text/xml")
            .expect()
                .statusCode(204).body(is(""))
            .when().put("/api/currentuser");
    }

    @Test
    public void putInvalidXml() throws Exception
    {
        given().body("<user xmlns=\"http://mulesoft.org/schemas/sample\" username=\"gbs\" firstName=\"george\" lastName=\"bernard shaw\">" +
                     "<email-addresses></email-addresses></user>")
                .contentType("text/xml")
            .expect()
                .statusCode(400).body(is("bad request"))
            .when().put("/api/currentuser");
    }
}