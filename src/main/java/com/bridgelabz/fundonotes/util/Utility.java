package com.bridgelabz.fundonotes.util;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bridgelabz.fundonotes.configuration.RabitMqConfig;
import com.bridgelabz.fundonotes.response.RabbitMessage;

@Component
public class Utility {

	private static final String SECRET = "abcdefghijklmnopqrstuvwxyzABCSEFGHIJKLMNOPQRSTUVWXYZ1234567890 ";

	@Autowired
	private JavaMailSender mailSender = null;

	public String jwtTokenGenerator(Long userId) {
		String token = null;
		try {
			token = JWT.create().withClaim("userId", userId).sign(Algorithm.HMAC512(SECRET));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return token;
	}

	public Long jwtTokenParser(String token) {
		Long user = null;
		if (token != null) {
			user = JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token).getClaim("userId").asLong();
		}
		return user;
	}

	public void javaMail(String to, String token, String url) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("shibadattanayak@gmail.com");
			message.setTo(to);
			message.setText(url + token);
			message.setSubject("Mail From BridgeLabz");
			mailSender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RabbitListener(queues = RabitMqConfig.queueName)
	public void sendToRabitMq(final Message<RabbitMessage> message) {
		RabbitMessage msg = message.getPayload();
		javaMail(msg.getEmail(), msg.getToken(), msg.getLink());
	}
}
