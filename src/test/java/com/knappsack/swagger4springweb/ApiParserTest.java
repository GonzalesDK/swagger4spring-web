package com.knappsack.swagger4springweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knappsack.swagger4springweb.parser.ApiParser;
import com.knappsack.swagger4springweb.parser.ApiParserImpl;
import com.wordnik.swagger.core.Documentation;
import com.wordnik.swagger.core.DocumentationEndPoint;
import com.wordnik.swagger.core.SwaggerSpec;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ApiParserTest extends AbstractTest {

    @Test
    public void testParseControllerDocumentation() {
        ApiParser apiParser = new ApiParserImpl(BASE_CONTROLLER_PACKAGE, BASE_MODEL_PACKAGE, "http://localhost:8080/api", "/api", "v1");
        Map<String, Documentation> documentList = apiParser.createDocuments();
        for (String key : documentList.keySet()) {
            Documentation documentation = documentList.get(key);
            ObjectMapper mapper = new ObjectMapper();
            try {
                String documentationAsJSON = mapper.writeValueAsString(documentation);
                System.out.println(documentationAsJSON);
                Documentation documentationDeserialized = mapper.readValue(documentationAsJSON, Documentation.class);
                assertNotNull(documentationDeserialized);
                assertTrue(documentationDeserialized.swaggerVersion().equals(SwaggerSpec.version()));
                assertTrue(documentationDeserialized.getApiVersion().equals("v1"));
                assertTrue(documentationDeserialized.getBasePath().equals("http://localhost:8080/api"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testResourceListing() {
        ApiParser apiParser = new ApiParserImpl(BASE_CONTROLLER_PACKAGE, BASE_MODEL_PACKAGE, "http://localhost:8080/api", "/api", "v1");
        Map<String, Documentation> documentList = apiParser.createDocuments();
        Documentation resourceList = apiParser.getResourceListing(documentList);
        assertTrue(resourceList.basePath().equals("http://localhost:8080/api"));
        assertTrue(resourceList.apiVersion().equals("v1"));
        assertTrue(resourceList.getApis().size() == 1);
        DocumentationEndPoint endPoint = resourceList.getApis().get(0);
        assertEquals(endPoint.getPath(), "/api/doc/api/v1/test");

    }
}
