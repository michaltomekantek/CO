package com.co.service;

import com.co.model.Pump;
import org.springframework.stereotype.Service;

@Service
public class PumpService {

    public boolean enablePump(Pump pump){
        pump.setEnabled(true);
        return true;
    }
}
