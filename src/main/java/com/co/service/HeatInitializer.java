package com.co.service;

import com.co.model.Flat;
import com.co.model.Heating;
import com.co.model.Pump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Service
public class HeatInitializer {

    private static Heating heating;
    private boolean enable = false;
    private List<Pump> pumps;

    @Autowired
    private FlatService flatService;

    @PostConstruct
    public void heatInit() {
        pumps = initPumps();

        heating = new Heating();
        heating.setHeatingFlats(flatService.getAvailableFlats());
        heating.setPumps(pumps);

    }

    public boolean isEnabled(){
        return enable;
    }

    public void enable(){
        enable = true;
    }

    public void disable(){
        enable = false;
    }


    public Heating getHeating() {
        return heating;
    }

    private List<Pump> initPumps() {
        List<Pump> pumps = new ArrayList<>();

        Pump waterPump = new Pump();
        waterPump.setName("Pompa wody");

        Pump gasHeatPump = new Pump();
        gasHeatPump.setName("Pompa pieca gazowego");

        pumps.add(waterPump);
        pumps.add(gasHeatPump);

        return pumps;
    }

}
