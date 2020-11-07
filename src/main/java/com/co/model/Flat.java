package com.co.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Flat {
    private String id;
    private String name;
    private boolean waterFlow = false;
    private boolean enabled = false;
    private String temeperatureDevice;
    private String gpio;
    private double actualTemperature;
    private double heatingTemperature;
}
