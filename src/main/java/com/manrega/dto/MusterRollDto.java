package com.manrega.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class MusterRollDto {
	private String panchayat;
	private MusterRollAttendance unskilledMusterRollAttendance;
	private MusterRollAttendance skilledMusterRollAttendance;
	private WageList wageList;
}
