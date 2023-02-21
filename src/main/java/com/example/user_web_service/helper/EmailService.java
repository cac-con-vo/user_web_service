package com.example.user_web_service.helper;

import com.example.user_web_service.entity.User;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private Environment env;

	public String getOTP() {
		Random r = new Random();
		int n = r.nextInt(999999);
		return String.format("%06d", n);
	}

	private void sent(User user) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			helper.setFrom(new InternetAddress(env.getProperty("spring.mail.username"), "Aus SuperMarket"));
			helper.setTo(user.getEmail());
			helper.setSubject("OTP Validation Code");
			helper.setText("use this code " + user.getCode());

			emailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendEmail(User user) {
		Runnable emailSender = () -> {
			this.sent(user);
		};
		Thread sendThread = new Thread(emailSender);
		sendThread.start();
	}
}
