package com.manrega.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("unused")
public class MusterRollAttendance {
	private int issued;
	private int filled;
	private int deleted;
	private int zeroAttendence;
}
