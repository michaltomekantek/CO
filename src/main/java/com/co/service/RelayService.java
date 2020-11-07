package com.co.service;

import com.co.model.Gpio;
import com.pi4j.io.gpio.*;
import com.pi4j.io.w1.W1Device;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class RelayService {
    final List<GpioPinDigitalOutput> gpioPinDigitalOutputs = new ArrayList<>();
    private HashMap<String, GpioPinDigitalOutput> gpioByFlat = new HashMap<>();

    @PostConstruct
    public void init() {
        final GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput gpioPinDigitalOutput1 =
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "GPIO_01", PinState.HIGH);

        if (gpioPinDigitalOutputs.stream().noneMatch(value -> value.getName().equals("GPIO_01"))) {
            gpioPinDigitalOutputs.add(gpioPinDigitalOutput1);
        }
        gpioByFlat.put("1", gpioPinDigitalOutputs.stream().
                filter(value -> "GPIO_01".equals(value.getName())).findFirst().get());
    }

    public List<Gpio> getAvailableOutputGpio() {
        List<Gpio> gpios = new ArrayList<>();
        gpioPinDigitalOutputs.forEach(value -> {
            Gpio gpio = new Gpio();
            gpio.setStatus(value.getState().toString());
            gpio.setType(value.getName());
            gpios.add(gpio);
        });
        return gpios;
    }

    public void enableGpio(String name) {
        Optional<GpioPinDigitalOutput> gpio = gpioPinDigitalOutputs.stream().filter(value -> name.equals(value.getName())).findFirst();
        if (gpio.isPresent()) {
            if(gpio.get().getState().isLow()){
                gpio.get().toggle();
            }
        }
    }

    public void disable(String name) {
        Optional<GpioPinDigitalOutput> gpio = gpioPinDigitalOutputs.stream().filter(value -> name.equals(value.getName())).findFirst();
        if (gpio.isPresent()) {
            if(gpio.get().getState().isHigh()){
                gpio.get().toggle();
            }
        }
    }

    public boolean addGpioToFlat(String deviceId, String flatId) {
        Optional<GpioPinDigitalOutput> gpioPinDigitalOutput = gpioPinDigitalOutputs.stream().filter(value -> deviceId.equals(value.getName())).findFirst();
        if (!gpioPinDigitalOutput.isPresent()) {
            return false;
        }
        if (!gpioByFlat.containsKey(flatId)) {
            gpioByFlat.put(flatId, gpioPinDigitalOutput.get());
            return true;
        }
        return false;
    }


}
