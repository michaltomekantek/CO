package com.co.controller;

import com.co.model.Flat;
import com.co.model.TemperatureDevice;
import com.co.service.FlatService;
import com.co.service.FlatTemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/temperatureDevice")
public class TemperatureDeviceController {

    @Autowired
    private FlatTemperatureService flatTemperatureService;

    @Autowired
    private FlatService flatService;

    @GetMapping(value = "/")
    public List<TemperatureDevice> getAvailableDevices() throws InterruptedException {
        return flatTemperatureService.getAvailableDevices();
    }

    @GetMapping(value = "/refreshDevices/")
    public boolean refresh() throws InterruptedException {
        flatTemperatureService.initW1Device();
        return true;
    }

    @GetMapping(value = "/temperatureDevices/add/")
    public boolean add(@RequestParam("flat_id") String flat_id, @RequestParam("device_id") String device_id) {
        Optional<Flat> flat = flatService.getAvailableFlats().stream().
                filter(value -> flat_id.equals(value.getId())).findFirst();

        if(flat.isPresent()){
            if(flatTemperatureService.addDeviceToFlat(device_id, flat_id)){
                flat.get().setTemeperatureDevice(device_id);
            }
            return true;
        }
        return false;
    }
}
