package com.wei.spring_boot_practice.service;

import com.wei.spring_boot_practice.entity.SendMailRequest;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class MailService {
    public void sendMail(SendMailRequest request) {
        final String host = "smtp.gmail.com";
        final int port = 587;
        final boolean enableAuth = true;
        final boolean enableStarttls = true;
        final String userAddress = "brighten.tso@omniwaresoft.com.tw";
        final String pwd = "brighten95";
        final String userDisplayName = "The one chasing the truth";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", String.valueOf(enableAuth));
        props.put("mail.smtp.starttls.enable", String.valueOf(enableStarttls));

        Session session = Session.getInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(userAddress, pwd);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setSubject(request.getSubject());
            message.setContent(request.getContent(), "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(userAddress, userDisplayName));
            for (String address : request.getReceivers()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            }

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

