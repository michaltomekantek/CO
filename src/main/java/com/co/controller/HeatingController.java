package com.co.controller;

import com.co.model.Flat;
import com.co.service.HeatInitializer;
import com.co.service.HeatingService;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.gpio.*;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/heating")
public class HeatingController {

    @Autowired
    private HeatingService heatingService;

    @Autowired
    private HeatInitializer heatInitializer;

    private static GpioPinDigitalOutput gpioPinDigitalOutput1;
    private static GpioPinDigitalOutput gpioPinDigitalOutput2;
    private static W1Master master;

    @GetMapping(value = "/start")
    public Boolean test() throws InterruptedException {
        heatingService.heating();
        return true;
    }

    @GetMapping(value = "/stop")
    public Boolean stop()  {
        heatingService.disable();
        return true;
    }

    @GetMapping(value = "/flats")
    public List<Flat> flats()  {
        return heatInitializer.getHeating().getHeatingFlats();
    }

    @GetMapping(value = "/ledOn")
    public Boolean on()  {
        if(gpioPinDigitalOutput1 == null){
            GpioController gpioController = GpioFactory.getInstance();
            gpioPinDigitalOutput1 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_01, "Led", PinState.LOW);
        }
        gpioPinDigitalOutput1.toggle();
        return true;
    }

    @GetMapping(value = "/releayOn")
    public Boolean relayOn()  {
        if(gpioPinDigitalOutput2 == null){
            GpioController gpioController = GpioFactory.getInstance();
            gpioPinDigitalOutput2 = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, "Relay", PinState.LOW);
        }
        gpioPinDigitalOutput2.toggle();
        return true;
    }

    @GetMapping(value = "/temperature")
    public String temperature()  {
        if (master == null) {
             master = new W1Master();
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<W1Device> w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
        for (W1Device device : w1Devices) {
            //this line is enought if you want to read the temperature
            String value_1 = "Temperature: " + ((TemperatureSensor) device).getTemperature();
            stringBuilder.append(value_1);
            stringBuilder.append("  ");
            //returns the temperature as double rounded to one decimal place after the point

            try {
                String value_2 = "1-Wire ID: " + device.getId() +  " value: " + device.getValue();
                //returns the ID of the Sensor and the  full text of the virtual file
                stringBuilder.append(value_2);
                stringBuilder.append("  ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}

