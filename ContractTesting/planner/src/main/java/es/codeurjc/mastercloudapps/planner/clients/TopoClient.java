package es.codeurjc.mastercloudapps.planner.clients;

import es.codeurjc.mastercloudapps.planner.models.LandscapeResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TopoClient {

    private static final String TOPO_HOST = "localhost";
    private static final int TOPO_PORT = 8080;

    private final RestTemplate restTemplate;

    public TopoClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
//            .rootUri("https://quotes.rest")
            .setConnectTimeout(Duration.ofSeconds(2))
            .setConnectTimeout(Duration.ofSeconds(2))
            .build();
    }

    @Async
    public CompletableFuture<String> getLandscape(String city) {
        
//        RestTemplate restTemplate = new RestTemplate();

        String url = "http://"+TOPO_HOST+":"+TOPO_PORT+"/api/topographicdetails/" + city;
        
        LandscapeResponse response = restTemplate.getForObject(url, LandscapeResponse.class);
        
        return CompletableFuture.completedFuture(response.getLandscape());
    }
}
