package com.benchmark.education.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class JavaMailUtil {


    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpMessage(String to, String subject, String message) throws MessagingException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        javaMailSender.send(msg);
    }

    public int sendOtp(String recipient) throws MessagingException {
        int otp = getRandomNumber();
        String template = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>OTP Email</title>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"font-family: Arial, sans-serif;\">\n" +
                "\n" +
                "    <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\">\n" +
                "\n" +
                "        <!-- Header -->\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#3498db\" style=\"padding: 20px; text-align: center; color: #ffffff; font-size: 24px; font-weight: bold;\">\n" +
                "                One-Time Password (OTP) Verification\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        <!-- Content -->\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px; font-size: 16px; line-height: 1.6;\">\n" +
                "                <p>Hello,</p>\n" +
                "                <p>Your One-Time Password (OTP) for verification is: <strong> "+ otp+"</strong></p>\n" +
                "                <p>Please use this code to complete the verification process.</p>\n" +
                "                <p>If you did not request this OTP, please ignore this email.</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        <!-- Footer -->\n" +
                "        <tr>\n" +
                "            <td bgcolor=\"#3498db\" style=\"padding: 20px; text-align: center; color: #ffffff; font-size: 14px;\">\n" +
                "                &copy; 2024 Your BenchMark Education. All rights reserved.\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "\n" +
                "    </table>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";
        sendOtpMessage(recipient, "Otp Verification",template);
        System.out.print("hitted");
        return otp;
    }

    public int getRandomNumber(){
        // Create an instance of the Random class
        Random random = new Random();

        // Generate a random 4-digit number
        int minRange = 1000; // minimum 4-digit number
        int maxRange = 9999; // maximum 4-digit number
        int randomFourDigitNumber = random.nextInt(maxRange - minRange + 1) + minRange;
        return randomFourDigitNumber;
    }

}
