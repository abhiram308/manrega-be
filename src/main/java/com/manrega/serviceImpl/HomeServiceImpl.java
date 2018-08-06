package com.manrega.serviceImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.manrega.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService{

	public String getData() {
		String data;
		try {
			Document doc = Jsoup.connect("http://www.baeldung.com/java-with-jsoup").get();
			data = doc.toString();
    		} catch (Exception ex) {
    			data = "something went wrong";
    		}
		return data;	
	}
}
