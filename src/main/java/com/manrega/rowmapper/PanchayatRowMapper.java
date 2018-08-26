package com.manrega.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.manrega.dto.MusterRollAttendance;
import com.manrega.dto.MusterRollDto;

public class PanchayatRowMapper implements RowMapper<MusterRollDto> {

	@Override
	public MusterRollDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		MusterRollDto musterRollDto = new MusterRollDto();
		musterRollDto.setPanchayat(rs.getString("panchayat_name"));
		MusterRollAttendance musterRollAttendance = new MusterRollAttendance();
		musterRollAttendance.setIssued(Integer.parseInt(rs.getString("issued")));
		musterRollAttendance.setFilled(Integer.parseInt(rs.getString("filled")));
		musterRollAttendance.setZeroAttendence(Integer.parseInt(rs.getString("zero_attendance")));
		musterRollAttendance.setDeleted(Integer.parseInt(rs.getString("deleted")));
		musterRollDto.setUnskilledMusterRollAttendance(musterRollAttendance);
		return musterRollDto;
	}

}
