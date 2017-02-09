package com.qualityirrelevant.web.controllers;

import com.qualityirrelevant.web.config.ApplicationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

import static javax.servlet.http.HttpServletResponse.SC_SERVICE_UNAVAILABLE;

@Controller
@RequestMapping("/contact")
public class ContactController {
  private final ApplicationProperties applicationProperties;

  public ContactController(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @GetMapping
  public String add(Model model) {
    model.addAttribute("name", "");
    model.addAttribute("email", "");
    model.addAttribute("message", "");
    model.addAttribute("nameError", "");
    model.addAttribute("emailError", "");
    model.addAttribute("messageError", "");
    return "contact";
  }

  @PostMapping
  public String create(Model model, HttpServletResponse response, @RequestParam String name,
                       @RequestParam String email, @RequestParam String message)
      throws Exception {
    Boolean hasError = false;
    String nameError = "";
    String emailError = "";
    String messageError = "";

    if (name.isEmpty()) {
      hasError = true;
      nameError = "Must not be fucking empty.";
    }
    if (email.isEmpty()) {
      hasError = true;
      emailError = "Must not be fucking empty.";
    }
    if (message.isEmpty()) {
      hasError = true;
      messageError = "Must not be fucking empty.";
    }
    if (hasError) {
      model.addAttribute("name", name);
      model.addAttribute("email", email);
      model.addAttribute("message", message);
      model.addAttribute("nameError", nameError);
      model.addAttribute("emailError", emailError);
      model.addAttribute("messageError", messageError);
      return "contact";
    }
    InternetAddress to = new InternetAddress("qualityirrelevant@gmail.com");
    InternetAddress from = new InternetAddress("qualityirrelevant@gmail.com");
    String subject = "Message from Contact Form";
    String body = "Name: " + name + "<br>Email: " + email + "<br>Message: " + message;

    Properties properties = new Properties();
    properties.setProperty("mail.smtp.host", applicationProperties.getSmtpHost());
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.starttls.enable", "true");
    properties.setProperty("mail.smtp.socketFactory.port", applicationProperties.getSmtpPort());
    properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.smtp.port", applicationProperties.getSmtpPort());

    Session session = Session.getInstance(properties,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(applicationProperties.getSmtpUsername(), applicationProperties.getSmtpPassword());
          }
        });

    MimeMessage mimeMessage = new MimeMessage(session);
    mimeMessage.setFrom(from);
    mimeMessage.addRecipient(Message.RecipientType.TO, to);
    mimeMessage.setSubject(subject);
    mimeMessage.setText(body);
    try {
      Transport.send(mimeMessage);
    } catch (MessagingException e) {
      model.addAttribute("error",
          "Unable to send your message. Fuck you. It has something to do with: "
              + e.getMessage());
      response.setStatus(SC_SERVICE_UNAVAILABLE);
      return "error";
    }
    return "redirect:/contact/success";
  }

  @GetMapping("/success")
  public String success() {
    return  "contact-success";
  }
}
