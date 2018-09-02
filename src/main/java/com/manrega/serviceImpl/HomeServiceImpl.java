package com.manrega.serviceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.manrega.repository.HomeRepository;
import com.manrega.service.HomeService;

@Component
public class HomeServiceImpl implements HomeService {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	HomeRepository homeRepository;

	private void getMusterData(String url) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			long panchayatCode = Long.parseLong(url.split("panchayat_code=")[1].split("&")[0]);
			Document doc = Jsoup.connect("http://mnregaweb4.nic.in/netnrega/state_html/" + url).get();
			Element musterTable = doc.select("tbody").get(0);
			Elements rows = musterTable.select("tr").next();
			for (Element element : rows) {
				int emr = Integer.parseInt(element.select("td").get(6).text());
				String date = element.select("td").get(7).text();
				String status = "filled";
				if ("".equals(date)) {
					status = "deleted";
				}
					
				String[] dates = date.split("--");
	            
	            java.sql.Date fromDate=new java.sql.Date(formatter.parse(dates[0]).getTime());
	            java.sql.Date toDate=new java.sql.Date(formatter.parse(dates[1]).getTime());
				
				System.out.println(panchayatCode + " ********* " + emr + " ========== " + dates[1] + "----- issued");
				homeRepository.update("insert into musters (emr, from_date, to_date, panchayat_code, status) values "
						+ "(" + emr + ", '" + fromDate + "', '" + toDate + "', " + panchayatCode
						+ ", '" + status + "') on duplicate" + " key update status='" + status + "'");

			}
		} catch (Exception Ex) {
			throw Ex;
		}
	}
	
	private void getMusterZeroAttendanceData(String url) throws Exception {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			long panchayatCode = Long.parseLong(url.split("panchayat_code=")[1].split("&")[0]);
			Document doc = Jsoup.connect("http://mnregaweb4.nic.in/netnrega/state_html/" + url).get();
			Element musterTable = doc.select("tbody").get(0);
			Elements rows = musterTable.select("tr").next();
			for (Element element : rows) {
				int emr = Integer.parseInt(element.select("td").get(6).text());
				String date = element.select("td").get(7).text();
				String status = "zero_attendance";
				
				String[] dates = date.split("--");
	            
	            java.sql.Date fromDate=new java.sql.Date(formatter.parse(dates[0]).getTime());
	            java.sql.Date toDate=new java.sql.Date(formatter.parse(dates[1]).getTime());
				
				System.out.println(panchayatCode + " ********* " + emr + " ========== " + dates[1] + "------ zero");
				homeRepository.update("insert into musters (emr, from_date, to_date, panchayat_code, status) values "
						+ "(" + emr + ", '" + fromDate + "', '" + toDate + "', " + panchayatCode
						+ ", '" + status + "') on duplicate" + " key update status='" + status + "'");

			}
		} catch (Exception Ex) {
			throw Ex;
		}
	}

	public String getData() {
		StringBuilder data = new StringBuilder();
		try {
			System.out.println("int service impl " + new Date());
			System.out.println("hit url " + new Date());
			Document doc = Jsoup
					.connect(
							"http://mnregaweb4.nic.in/netnrega/state_html/emuster_wagelist_rpt.aspx?page=B&lflag=eng&state_name=MAHARASHTRA&state_code=18&district_name=BHANDARA&district_code=1828&block_name=MOHADI&block_code=1828027&fin_year=2018-2019&Digest=ZhvFKE7Zig/sCm5PlZFngw")
					.get();
			System.out.println("hit url end" + new Date());

			Element musterRollTable = doc.select("tbody").get(2);

			Elements rows = musterRollTable.select("tr").next().next().next();

			int issued = 0;
			int filled = 0;
			int deleted = 0;
			int zeroAttendance = 0;
			for (Element element : rows) {
				// Elements columns = element.select("td");
				String panchayatName = element.select("td").get(1).text();
				String issuedUrl = element.select("td").get(2).select("a").attr("href");
				String zeroAttendanceUrl = element.select("td").get(5).select("a").attr("href");
				issued += Integer.parseInt(element.select("td").get(2).text());
				filled += Integer.parseInt(element.select("td").get(3).text());
				deleted += Integer.parseInt(element.select("td").get(4).text());
				zeroAttendance += Integer.parseInt(element.select("td").get(5).text());
				int totalIssued = issued - deleted - zeroAttendance;
				int active = totalIssued - filled;
				// System.out.println(issuedUrl);
				homeRepository.update("update panchayats set issued=" + issued + ", filled=" + filled + ", deleted="
						+ deleted + ", zero_attendance=" + zeroAttendance + ", total_issued=" + totalIssued
						+ ", active=" + active + " where panchayat_name='" + panchayatName + "'");
				if (issued == 55) {
					getMusterData(issuedUrl);
					getMusterZeroAttendanceData(zeroAttendanceUrl);	
				}
//				if (filled == 54)
//					getMusterData(filledUrl);
				// homeRepository.update("insert into blocks (block_code,
				// block_name, active,"
				// + "total_issued, filled, deleted, zero_attendance, fy,
				// district_code)"
				// + " values (1234, 'abc', 12345, 3, 4, 5, 6, 2019, 1828)");
				// data = data.append("<p>" + element.text() + "</p>");
			}
//			int totalIssued = issued - deleted - zeroAttendance;
//			int active = totalIssued - filled;
//			int blockCode = 1828027;
//			String blockName = "MOHADI";
//			int fy = 2019;
//			int districtCode = 1828;
//
//			String sql = "hi";
//			List<MusterRollDto> queryList = homeRepository.query(sql);
//			for (MusterRollDto dto : queryList) {
//				data = data.append("<p> " + dto.getPanchayat() + " "
//						+ dto.getUnskilledMusterRollAttendance().getFilled() + " " + "</p>");
//			}

			System.out.println("service impl completed " + new Date());

		} catch (Exception ex) {
			data.append("Exception occured" + ex);
		}
		return data.toString();
	}
}
