package com.bridgelabz.fundonotes.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class Utility {

	private static final String SECRET = "abcdefghijklmnopqrstuvwxyzABCSEFGHIJKLMNOPQRSTUVWXYZ1234567890 ";

	@Autowired
	private JavaMailSender mailSender;

	public String isValidPassword(String password) {
		int length = password.trim().length();
		int digit = 0;
		int lowerCase = 0;
		int upperCase = 0;
		int specialChar = 0;
		int count = 0;
		char ch = '\u0000';

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
			return "Strength is strong";
		else
			return "Strength is low";
	}

	public String jwtToken(Long userId) {
		String token = null;
		try {
			token = JWT.create().withClaim("userId", userId).sign(Algorithm.HMAC512(SECRET));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return token;
	}

	public Integer jwtTokenParser(String token) {
		Integer user = 0;
		if (token != null) {
			user = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token).getClaim("userId").asInt();
		}
		return user;
	}

	public void javaMail(String to, String token, String url) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("sibadattanayak1996@gmail.com");
			message.setTo(to);
			message.setText(url + token);
			message.setSubject("IsVerified Token");
			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
