package com.bridgelabz.fundonotes.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

	public boolean isValidPassword(String password) {
		int length = password.trim().length();
		int digit = 0;
		int lowerCase = 0;
		int upperCase = 0;
		int specialChar = 0;
		int count = 0;
		char ch;

		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
		Matcher matcher = pattern.matcher(password);

		if (length >= 4) {
			while (count < length) {
				ch = password.trim().charAt(count);
				if (Character.isDigit(ch)) {
					digit = digit + 1;
				}
				if (Character.isLowerCase(ch)) {
					lowerCase = lowerCase + 1;
				}
				if (Character.isUpperCase(ch)) {
					upperCase = upperCase + 1;
				}
				if (!matcher.matches()) {
					specialChar = specialChar + 1;
				}
				count = count + 1;
			}
		}
		if (digit >= 1 && lowerCase >= 1 && upperCase >= 1 && specialChar >= 1)
			return true;
		else
			return false;
	}

}
