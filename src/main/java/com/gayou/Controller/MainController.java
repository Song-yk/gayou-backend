package com.gayou.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index"; //templates 폴더의 index.html을 찾아감
    }
    @GetMapping("/api/hello")
    public String test() {
        return "Hello, world!";
    }
}