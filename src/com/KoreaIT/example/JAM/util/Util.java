package com.KoreaIT.example.JAM.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {
	public static String datetimeFormat(LocalDateTime datetime) {
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(datetime);
	}
}