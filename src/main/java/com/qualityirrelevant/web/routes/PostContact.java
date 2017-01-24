package com.qualityirrelevant.web.routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.qualityirrelevant.web.Application.smtpHost;
import static com.qualityirrelevant.web.Application.smtpPassword;
import static com.qualityirrelevant.web.Application.smtpPort;
import static com.qualityirrelevant.web.Application.smtpUsername;

public class PostContact extends FreeMarkerRoute {
  public PostContact(FreeMarkerEngine freeMarkerEngine, String viewName) {
    super(freeMarkerEngine, viewName);
  }

  @Override
  public ModelAndView run(Request request, Response response) throws Exception {
    String name = trim(request.queryParams("name"));
    String email = trim(request.queryParams("email"));
    String message = trim(request.queryParams("message"));
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
      Map<String, String> model = new HashMap<>();
      model.put("name", name);
      model.put("email", email);
      model.put("message", message);
      model.put("nameError", nameError);
      model.put("emailError", emailError);
      model.put("messageError", messageError);
      return new ModelAndView(model, getViewName());
    }
    InternetAddress to = new InternetAddress("qualityirrelevant@gmail.com");
    InternetAddress from = new InternetAddress("qualityirrelevant@gmail.com");
    String subject = "Message from Contact Form";
    String body = "Name: " + name + "<br>Email: " + email + "<br>Message: " + message;

    Properties properties = new Properties();
    properties.setProperty("mail.smtp.host", smtpHost);
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.smtp.starttls.enable", "true");
    properties.setProperty("mail.smtp.socketFactory.port", smtpPort);
    properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    properties.setProperty("mail.smtp.port", smtpPort);

    Session session = Session.getInstance(properties,
        new javax.mail.Authenticator() {
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(smtpUsername, smtpPassword);
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
      Map<String, String> model = new HashMap<>();
      model.put("errorMessage",
          "Unable to send your message. Fuck you. It has something to do with: "
              + e.getMessage());
      response.status(503);
      return new ModelAndView(model, "error.ftl");
    }
    response.redirect("/contact/success");
    return null;
  }

  private static String trim(String x) {
    return x == null ? "" : x.trim();
  }
}
