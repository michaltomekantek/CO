package com.co.service;

import com.co.model.Flat;
import com.co.model.Heating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlatService {
    private FlatTemperatureService flatTemperatureService;
    private List<Flat> flats;

    @Autowired
    private FlatService(FlatTemperatureService flatTemperatureService){
        this.flatTemperatureService = flatTemperatureService;

    }

    @PostConstruct
    public void init() {
        flats = initFlats();
    }

    private List<Flat> initFlats() {
        List<Flat> flats = new ArrayList<>();
        Flat flatTomek = new Flat();
        flatTomek.setId("1");
        flatTomek.setGpio("GPIO_01");
        flatTomek.setTemeperatureDevice("28-011876ef82ff");
        flatTomek.setEnabled(true);
        flatTomek.setHeatingTemperature(25);
        flatTomek.setName("Mieszkanie Tomek");

        Flat flatKajtek = new Flat();
        flatKajtek.setId("2");
        flatKajtek.setHeatingTemperature(18);
        flatKajtek.setEnabled(false);
        flatKajtek.setName("Mieszkanie Kajtek");

        Flat flatUla = new Flat();
        flatUla.setId("3");
        flatUla.setHeatingTemperature(18);
        flatUla.setEnabled(false);
        flatUla.setName("Mieszkanie Ula");

        flats.add(flatKajtek);
        flats.add(flatTomek);
        flats.add(flatUla);

        return flats;
    }

    public List<Flat> getAvailableFlats(){
        return flats;
    }

    public boolean enableFLatHeating(Flat flat){
        flat.setEnabled(true);
        return true;
    }

    public boolean disableFlatHeating(Flat flat){
        flat.setEnabled(false);
        return true;
    }

    public boolean checkWaterFlow(Flat flat){
        boolean waterFlow = flat.isEnabled() && flat.getActualTemperature() < flat.getHeatingTemperature();
        flat.setWaterFlow(waterFlow);
        return flat.isWaterFlow();
    }

    public double setFlatTemperature(Flat flat){
        flat.setActualTemperature(flatTemperatureService.getFlatTemperature(flat));
        return flat.getActualTemperature();
    }
}
