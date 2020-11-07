package com.co.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Heating {
    private List<Flat> heatingFlats = new ArrayList<>();
    private List<Pump> pumps = new ArrayList<>();
}
