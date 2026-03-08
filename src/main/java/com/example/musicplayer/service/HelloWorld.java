package com.example.musicplayer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {
    
    @Autowired
    private MessageService messageService;

    @GetMapping("/hello")
    public String hello() {
       
        return messageService.getMessage();

    }
}
