package com.qualityirrelevant.web.models;

import com.qualityirrelevant.web.Application;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Episode {
  private Long id = 0L;
  private String name = "";
  private String description = "";
  private Timestamp publishedOn = new Timestamp(System.currentTimeMillis());
  private Long duration = 0L;
  private Long size = 0L;
  private Long number = 0L;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return "/media/episodes/" + id + ".mp3";
  }

  public String getAbsoluteUrl() {
    return Application.baseUrl + getUrl();
  }

  public String getTitle() {
    return "#" + getNumber() + ": " + getName();
  }

  public String getPublishedOn() {
    SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy");
    return formatter.format(publishedOn);
  }

  public void setPublishedOn(Timestamp publishedOn) {
    this.publishedOn = publishedOn;
  }

  public String getFullPublishedOn() {
    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    return formatter.format(publishedOn);
  }

  public String getFormattedDuration() {
    Long hours = duration / 3600;
    Long minutes = (duration % 3600) / 60;
    Long seconds = duration % 60;
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public Long getDuration() {
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = duration;
  }

  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }

  public Long getNumber() {
    return number;
  }

  public void setNumber(Long number) {
    this.number = number;
  }

  public void setNumber(String number) {
    if ("".equals(number)) {
      number = "0";
    }
    this.number = Long.parseLong(number);
  }
}
