package com.manrega.serviceImpl;

import java.util.List;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.manrega.dto.MusterRollDto;
import com.manrega.repository.HomeRepository;
import com.manrega.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	HomeRepository homeRepository;
	
	public String getData() {
		StringBuilder data = new StringBuilder();
		try {
			System.out.println("int service impl " + new Date());
			System.out.println("hit url " + new Date());
			Document doc = Jsoup.connect("http://mnregaweb4.nic.in/netnrega/state_html/emuster_wagelist_rpt.aspx?page=B&lflag=eng&state_name=MAHARASHTRA&state_code=18&district_name=BHANDARA&district_code=1828&block_name=MOHADI&block_code=1828027&fin_year=2018-2019&Digest=ZhvFKE7Zig/sCm5PlZFngw").get();
			System.out.println("hit url end" + new Date());
			
			Element musterRollTable = doc.select("tbody").get(2);
			
			Elements rows = musterRollTable.select("tr").next().next().next();
			
			
			int issued = 0;
			int filled = 0;
			int deleted = 0;
			int zeroAttendance = 0;
			for (Element element : rows) {
                //Elements columns = element.select("td");
				String panchayatName = element.select("td").get(1).text();
                issued +=     Integer.parseInt(element.select("td").get(2).text());
                filled +=     Integer.parseInt(element.select("td").get(3).text());
                deleted +=    Integer.parseInt(element.select("td").get(4).text());
                zeroAttendance +=    Integer.parseInt(element.select("td").get(5).text());
                int totalIssued = issued - deleted - zeroAttendance;
    			int active = totalIssued - filled;
                homeRepository.update("update panchayats set issued=" + issued + ", filled=" + filled
                		+ ", deleted=" + deleted + ", zero_attendance=" + zeroAttendance 
                		+ ", total_issued=" + totalIssued + ", active=" + active + " where panchayat_name='" + panchayatName +"'");
//                homeRepository.update("insert into blocks (block_code, block_name, active,"
//					+ "total_issued, filled, deleted, zero_attendance, fy, district_code)"
//					+ " values (1234, 'abc', 12345, 3, 4, 5, 6, 2019, 1828)");
//                data = data.append("<p>" + element.text() + "</p>");
            }
			data = data.append("<h1>" + filled / 2 + "</h1><br>");
			data = data.append("<h1>" + issued / 2 + "</h1><br>");
			int totalIssued = issued - deleted - zeroAttendance;
			int active = totalIssued - filled;
			int blockCode = 1828027;
			String blockName = "MOHADI";
			int fy = 2019;
			int districtCode = 1828;
			
			String sql = "hi";
			List<MusterRollDto> queryList = homeRepository.query(sql);
			for(MusterRollDto dto: queryList) {
				data = data.append("<p> " + dto.getPanchayat() + " " + dto.getUnskilledMusterRollAttendance().getFilled() + " " + "</p>");
			}
			//			jdbcTemplate.execute("insert into blocks (block_code, block_name, active,"
//					+ "total_issued, filled, deleted, zero_attendance, fy, district_code)"
//					+ " values (1234, 'abc', 12345, 3, 4, 5, 6, 2019, 1828)");
//			for (Element row : rows) {
//				data = data.append("<p>" + row.text() + "</p></b></br>");
//			}
			
			System.out.println("service impl completed " + new Date());

		} catch (Exception ex) {
			data.append("Exception occured" + ex);
		}
		return data.toString();
	}
}
