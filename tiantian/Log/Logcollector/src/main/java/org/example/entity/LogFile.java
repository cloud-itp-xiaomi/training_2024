package org.example.entity;

public class LogFile {
  private String hostname;
  private String file;
  //private String content;
  private String[] content;

  public LogFile(){}

  public LogFile(String hostname, String file, String[] content){
    this.hostname = hostname;
    this.file = file;
    this.content = content;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String[] getContent() {
    return content;
  }

  public void setContent(String[] content) {
    this.content = content;
  }
}
