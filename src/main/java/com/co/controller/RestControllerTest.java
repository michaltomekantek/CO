package com.co.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestControllerTest {


    @GetMapping(value = "/test")
    public List<String> test(){
        return new ArrayList<>();
    }
}

