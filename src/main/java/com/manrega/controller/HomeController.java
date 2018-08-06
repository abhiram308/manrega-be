package com.manrega.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manrega.service.HomeService;


@RestController
public class HomeController {

	@Autowired
	HomeService homeService;
	
    @RequestMapping("/")
    public String index() {
    	return homeService.getData();
    }

}
