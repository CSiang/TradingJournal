package tfip_project.financial_analysis.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender emailSender;

    // To ensure this work, will have to disable Avast MailShield. It will block email sent by this app.
    public void sendSimpleMessage(String to, String subject, String text) {
        
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply@gmail.com");
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
        
    }

}
