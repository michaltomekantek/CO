package com.co.controller;

import com.co.model.Flat;
import com.co.model.Gpio;
import com.co.service.FlatService;
import com.co.service.RelayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/relay")
public class RelayController {

    @Autowired
    private RelayService relayService;

    @Autowired
    private FlatService flatService;

    @GetMapping(value = "/")
    public List<Gpio> getAvailableGpio() {
        return relayService.getAvailableOutputGpio();
    }

    @GetMapping(value = "/init")
    public boolean init() {
        relayService.init();
        return true;

    }

    @GetMapping(value = "/test")
    public boolean testGPIO(@RequestParam String gpioName) {
        relayService.enableGpio(gpioName);
        return true;
    }

    @GetMapping(value = "/gpio/add/")
    public boolean add(@RequestParam("flat_id") String flat_id, @RequestParam("gpio_id") String gpio_id) {
        Optional<Flat> flat = flatService.getAvailableFlats().stream().
                filter(value -> flat_id.equals(value.getId())).findFirst();

        if(flat.isPresent()){
            if(relayService.addGpioToFlat(gpio_id, flat_id)){
                flat.get().setGpio(gpio_id);
            }
            return true;
        }
        return false;
    }
}
