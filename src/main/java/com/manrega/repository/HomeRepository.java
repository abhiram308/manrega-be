package com.manrega.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.manrega.dto.MusterRollDto;
import com.manrega.rowmapper.PanchayatRowMapper;

@Repository
public class HomeRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void insert(String sql) {
		
	}
	
	public List<MusterRollDto> query(String sql) {
		 return jdbcTemplate.query("select * from panchayats", new PanchayatRowMapper());
	}
	
	public void update(String sql) {
		jdbcTemplate.execute(sql);
	}
}
