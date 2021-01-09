package com.cisco.jwt_spring_boot.services;

import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SendMailService implements SendMail{

    public boolean sendPasswordRecoverMail(String toEmail) {
        String recoverCode = Base64.getEncoder().encodeToString(toEmail.getBytes());
        try {
            String subject = "Récupération de votre mot de passe";
            String body = "Merci de cliquer sur le lien suivant : " + "http://localhost:4200/login/recover/" + recoverCode + " pour récupérer votre mot de passe.";
            sendEmail(toEmail, subject, body);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }


    @Override
    public boolean sendVerificationMail(String toEmail, String verificationCode) {

        try {
            String subject = "Vérification de votre adresse email";
            String body = "Merci de confirmer votre adresse email en cliquant sur le lien suivant : " + "http://localhost:4200/verify-email/" + verificationCode;
            sendEmail(toEmail, subject, body);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean sendUserCredentials(String toEmail, String username, String password) {
        try {
            String subject = "Informations de connexion";
            String body = "Suite à la confirmation de votre adresse email vous pouvez vous connecter avec les identifiants suivants: \n Username : " + username + " \n Mot de passe : " + password + " \n Rendez-vous sur http://localhost:4200/login ";
            sendEmail(toEmail, subject, body);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    /**
     * Utility method to send simple HTML email
     * @param toEmail
     * @param subject
     * @param body
     */
    @Override
    public void sendEmail(String toEmail, String subject, String body){
        final String username = "pipondiogoye@gmail.com";
        final String password = "ndione25";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText("Dear Mail Crawler,"
                    + "\n\n Please do not spam my email!" + body);

            System.out.println("Done");
            Transport.send(message);
            System.out.println("Email Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
