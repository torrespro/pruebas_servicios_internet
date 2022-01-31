package es.codeurjc.mastercloudapps.planner.clients.pact.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import es.codeurjc.mastercloudapps.planner.clients.TopoClient;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;

/**
 * Pact consumer tests for interactions between the Planner service and Topo Service api endpoints. This class has a
 * PactConsumer JUnit 5 annotation tag to allow all consumer tests to be run in a convenient manner.
 */
@Tag("PactConsumer")
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "topo_service", port = "8080")
class TopoServiceApiConsumerPactTest {

    private static final Map<String, String> DEFAULT_RESPONSE_HEADERS =
        Collections.singletonMap("Content-Type", "application/json");

    private TopoClient client = new TopoClient(new RestTemplateBuilder());

    @Pact(consumer = "planner_service")
    public RequestResponsePact getTopo(PactDslWithProvider builder) {
        return builder
            .given("a landscape is returned")
            .uponReceiving("a valid request")
            .matchPath("/api/topographicdetails/Madrid")
            .method("GET")
            .willRespondWith()
            .status(200)
            .headers(Map.of("Content-Type", "application/json"))
            .body(new PactDslJsonBody()
                .stringType("id", "Madrid")
                .stringType("landscape", "Flat"))
            .headers(DEFAULT_RESPONSE_HEADERS)
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getTopo")
    void testGetTopo() throws ExecutionException, InterruptedException {

        CompletableFuture<String> landscape = client.getLandscape("Madrid");

        assertEquals("Flat", landscape.get());
    }
}
