CREATE DATABASE IF NOT EXISTS jiu_log_monitor;

USE jiu_log_monitor;

CREATE TABLE IF NOT EXISTS logs (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    hostname VARCHAR(255) NOT NULL,
                                    file VARCHAR(255) NOT NULL,
                                    log TEXT NOT NULL,
                                    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);