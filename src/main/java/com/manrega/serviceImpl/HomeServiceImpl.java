package com.manrega.serviceImpl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import com.manrega.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService {

	public String getData() {
		StringBuilder data = new StringBuilder();
		try {
			Document doc = Jsoup.connect("https://www.ibm.com/developerworks/cloud/library/cl-add-an-organization-to-your-hyperledger-fabric-blockchain/index.html").get();

			for (Element element : doc.select("a")) {
				data = data.append("<p>" + element.text() + "</p><b>" + element.attr("href") + "</b></br>");
			}

		} catch (Exception ex) {
			data.append("Exception occured");
		}
		return data.toString();
	}
}
