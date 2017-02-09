package com.qualityirrelevant.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StaticController {
  @GetMapping("/art")
  public String art() {
    return "art";
  }
}
