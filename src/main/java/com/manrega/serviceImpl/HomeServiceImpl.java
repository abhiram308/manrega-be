package com.manrega.serviceImpl;

import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.manrega.service.HomeService;
import com.mysql.fabric.xmlrpc.base.Data;

@Component
public class HomeServiceImpl implements HomeService {

	public String getData() {
		StringBuilder data = new StringBuilder();
		try {
			System.out.println("int service impl " + new Date());
			System.out.println("hit url " + new Date());
			Document doc = Jsoup.connect("http://mnregaweb4.nic.in/netnrega/state_html/emuster_wagelist_rpt.aspx?page=B&lflag=eng&state_name=MAHARASHTRA&state_code=18&district_name=BHANDARA&district_code=1828&block_name=MOHADI&block_code=1828027&fin_year=2018-2019&Digest=ZhvFKE7Zig/sCm5PlZFngw").get();
			System.out.println("hit url end" + new Date());
			
			Element musterRollTable = doc.select("tbody").get(2);
			
			Elements rows = musterRollTable.select("tr");
			
			for (Element row : rows) {
				data = data.append("<p>" + row.text() + "</p></b></br>");
			}
			System.out.println("service impl completed " + new Date());

		} catch (Exception ex) {
			data.append("Exception occured");
		}
		return data.toString();
	}
}
