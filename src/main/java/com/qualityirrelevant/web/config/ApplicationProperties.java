package com.qualityirrelevant.web.config;

public class ApplicationProperties {
  private String baseUrl = "https://qualityirrelevant.com";
  private String port = "4567";
  private String authorizedIp = "";
  private String smtpUsername = "qualityirrelevant@gmail.com";
  private String smtpPassword = "";
  private String smtpHost = "smtp.gmail.com";
  private String smtpPort = "465";

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getAuthorizedIp() {
    return authorizedIp;
  }

  public void setAuthorizedIp(String authorizedIp) {
    this.authorizedIp = authorizedIp;
  }

  public String getSmtpUsername() {
    return smtpUsername;
  }

  public void setSmtpUsername(String smtpUsername) {
    this.smtpUsername = smtpUsername;
  }

  public String getSmtpPassword() {
    return smtpPassword;
  }

  public void setSmtpPassword(String smtpPassword) {
    this.smtpPassword = smtpPassword;
  }

  public String getSmtpHost() {
    return smtpHost;
  }

  public void setSmtpHost(String smtpHost) {
    this.smtpHost = smtpHost;
  }

  public String getSmtpPort() {
    return smtpPort;
  }

  public void setSmtpPort(String smtpPort) {
    this.smtpPort = smtpPort;
  }
}
