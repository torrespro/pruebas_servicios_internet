package es.codeurjc.mastercloudapps.planner.clients;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@RestClientTest(TopoClient.class)
class TopoClientTest {

    @Autowired
    private TopoClient topoClient;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    @DisplayName("should return landscape from topo service")
    public void shouldReturnLandscapeFromTopoService() throws ExecutionException, InterruptedException {
        String response = """
                {
                  "id": "Madrid",
                  "landscape": "Flat"
                }    
            """;

        this.mockRestServiceServer
            .expect(MockRestRequestMatchers.requestTo("http://localhost:8080/api/topographicdetails/Madrid"))
            .andRespond(MockRestResponseCreators.withSuccess(response, MediaType.APPLICATION_JSON));

        CompletableFuture<String> result = topoClient.getLandscape("Madrid");

        assertEquals("Flat", result.get());
    }
}
