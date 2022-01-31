package es.codeurjc.mastercloudapps.topo.controller;

import es.codeurjc.mastercloudapps.topo.model.City;
import es.codeurjc.mastercloudapps.topo.model.CityDTO;
import es.codeurjc.mastercloudapps.topo.service.TopoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/topographicdetails")
public class TopoController {

    private final TopoService topoService;

    public TopoController(TopoService topoService) {
        this.topoService = topoService;
    }

    @GetMapping("/{city}")
    public CityDTO getCity(@PathVariable String city) {
        return toCityDTO(topoService.getCity(city));
    }

    private CityDTO toCityDTO(City city) {
        return new CityDTO(city.getId(), city.getLandscape());
    }

}
