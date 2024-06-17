package org.example.entity;

import javax.persistence.*;


@Entity
@Table(name = "logs2")
public class LogEntry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "hostname")
  private String hostname;
  @Column(name = "file", length = 255)
  private String file;
  @Column(name = "content", length = 10000)
  @Convert(converter = JsonStringArrayConverter.class)
  private String[] content;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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