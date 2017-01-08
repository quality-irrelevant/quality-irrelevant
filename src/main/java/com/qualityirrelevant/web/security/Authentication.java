package com.qualityirrelevant.web.security;

import com.qualityirrelevant.web.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

public class Authentication {
  private static final Logger logger = LoggerFactory.getLogger(Authentication.class);

  public static void authenticate(Request request) {
    String authorizedIp = Application.authorizedIp;
    String currentIp = request.ip();
    String forwardedIp = request.headers("X-Forwarded-For");

    logger.info("Authorised IP: {}; Current IP: {}; Forwarded IP: {}", authorizedIp,
        currentIp, forwardedIp);

    if (forwardedIp != null) {
      currentIp = forwardedIp;
    }

    if (!currentIp.equals(authorizedIp)) {
      throw new UnauthenticatedException("Fuck off, you're not allowed here sonny jim!");
    }
  }

  public static Boolean isAuthenticated(Request request) {
    try {
      authenticate(request);
    } catch (UnauthenticatedException e) {
      return false;
    }
    return true;
  }
}
