package com.co.service;

import com.co.model.Flat;
import com.co.model.Heating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class HeatingService {
    private HeatInitializer heatInitializer;
    private FlatService flatService;
    private RelayService relayService;

    @Autowired
    public HeatingService(HeatInitializer heatInitializer, FlatService flatService,
                          RelayService relayService){
        this.heatInitializer = heatInitializer;
        this.flatService = flatService;
        this.relayService = relayService;
    }

    @Async
    public void heating() throws InterruptedException {
        if(!heatInitializer.isEnabled()){
            heatInitializer.enable();
            while(heatInitializer.isEnabled()){
                Thread.sleep(2000);

                Heating heating = heatInitializer.getHeating();

                heating.getHeatingFlats().stream().filter(Flat::isEnabled).forEach(flat ->{
                    flatService.setFlatTemperature(flat);
                    if(flatService.checkWaterFlow(flat)){
                        relayService.enableGpio(flat.getGpio());
                    }else{
                        relayService.disable(flat.getGpio());
                    }
                });
            }
        }else{
            System.out.println("Already working");
        }
    }

    public void disable(){
        heatInitializer.disable();
        System.out.println("Disable");
    }




}
