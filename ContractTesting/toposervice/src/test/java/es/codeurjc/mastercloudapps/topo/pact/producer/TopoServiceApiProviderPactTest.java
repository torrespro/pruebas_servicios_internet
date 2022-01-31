package es.codeurjc.mastercloudapps.topo.pact.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import es.codeurjc.mastercloudapps.topo.controller.TopoController;
import es.codeurjc.mastercloudapps.topo.model.City;
import es.codeurjc.mastercloudapps.topo.service.TopoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Pact provider tests for interactions between the Planner service and Topo service api endpoints. It uses Spring to
 * configure certain MVC features without requiring the whole Spring Boot context to keep the tests quick. This class
 * has a PactProvider JUnit 5 annotation tag to allow all provider tests to be run in a convenient manner.
 */
@Tag("PactProvider")
@PactFolder("pacts")
//@PactBroker(url = "http://pactbroker:9292", authentication = @PactBrokerAuth(username = "pact_workshop", password = "pact_workshop"))
@Provider("topo_service")
@Consumer("planner_service")
@ExtendWith({SpringExtension.class})
class TopoServiceApiProviderPactTest {

    private final TopoService service;

    public TopoServiceApiProviderPactTest() {
        service = Mockito.mock(TopoService.class);
    }

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        if (context != null) {
            context.verifyInteraction();
        }
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        if (context != null) {
            context.setTarget(new MockMvcTestTarget(MockMvcBuilders
                .standaloneSetup(new TopoController(service))
                .build()));
        }
    }

    @State("a landscape is returned")
    public void landscapeIsReturned() {
        when(service.getCity(any(String.class))).thenReturn(new City("Madrid", "Flat"));
    }

}
