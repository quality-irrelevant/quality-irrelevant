package com.qualityirrelevant.web.email;

import com.icegreen.greenmail.util.GreenMail;

public class GreenMailEmailService implements EmailService {
  private final GreenMail greenMail;

  public GreenMailEmailService(GreenMail greenMail) {
    this.greenMail = greenMail;
  }

  @Override
  public void start() {
    greenMail.start();
  }
}
