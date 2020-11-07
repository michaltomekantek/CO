package com.co.controller;

import com.co.model.Flat;
import com.co.service.FlatService;
import com.co.service.FlatTemperatureService;
import com.co.service.HeatInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/flat")
public class FlatController {

    @Autowired
    private FlatService flatService;

    @Autowired FlatTemperatureService flatTemperatureService;

    @GetMapping(value = "/")
    public List<Flat> getAvailableFlats() {
        flatService.getAvailableFlats().forEach(value->{
            value.setActualTemperature(flatTemperatureService.getFlatTemperature(value));
        });

        return flatService.getAvailableFlats();
    }

    @PostMapping(value = "/create")
    public List<Flat> createFlat() throws InterruptedException {
        return null;
    }
}
