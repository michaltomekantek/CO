package com.co.service;

import com.co.model.Flat;
import com.co.model.TemperatureDevice;
import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.impl.TmpDS18B20DeviceType;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class FlatTemperatureService {
    private static W1Master master;
    private List<W1Device> w1Devices;
    private HashMap<String, W1Device> temperatureDeviceByFlat = new HashMap<>();

    @PostConstruct
    public void init() {
        master = new W1Master();
        initW1Device();
    }

    public void initW1Device() {
        w1Devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
        temperatureDeviceByFlat.put("1", w1Devices.stream().
                filter(value -> "28-011876ef82ff".equals(value.getId())).findFirst().get());
    }

    public List<TemperatureDevice> getAvailableDevices() {
        List<TemperatureDevice> temperatureDevices = new ArrayList<>();
        for (W1Device device : w1Devices) {
            TemperatureDevice temperatureDevice = new TemperatureDevice();
            temperatureDevice.setTemperature("" + ((TemperatureSensor) device).getTemperature());
            try {
                temperatureDevice.setDeviceId(device.getId());
                temperatureDevice.setDeviceFile(device.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
            temperatureDevices.add(temperatureDevice);
        }
        return temperatureDevices;
    }

    public double getFlatTemperature(Flat flat) {
        W1Device w1Device = temperatureDeviceByFlat.get(flat.getId());
        if (w1Device != null) {
            return ((TemperatureSensor) w1Device).getTemperature();
        }
        return -1;
    }

    public boolean addDeviceToFlat(String deviceId, String flatId) {
        Optional<W1Device> w1Device = w1Devices.stream().filter(value -> deviceId.equals(value.getId())).findFirst();
        if (!w1Device.isPresent()) {
            return false;
        }
        if (!temperatureDeviceByFlat.containsKey(flatId)) {
            temperatureDeviceByFlat.put(flatId, w1Device.get());
            return true;
        }
        return false;
    }
}
