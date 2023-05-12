package com.virtuslab.vssjava.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HashController {

    @RequestMapping("/hash")
    String dummyGetEndpoint() {
        return "Hello Hash!";
    }
}
