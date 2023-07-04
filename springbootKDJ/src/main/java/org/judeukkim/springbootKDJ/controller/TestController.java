package org.judeukkim.springbootKDJ.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public String hi(){
        return "<h1>hi judeukkim</h1>";
    }
}
