package com.h_s.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_entry")
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "hostname")
    private String hostname;
    @Column(name = "file")
    private String file;
    @OneToMany(mappedBy = "logEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LogEntryLogs> logs;
}
