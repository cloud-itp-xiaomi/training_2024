CREATE TABLE `Metrics` (
                          `id` INT AUTO_INCREMENT PRIMARY KEY,
                          `metric` VARCHAR(255) NOT NULL,
                          `endpoint` VARCHAR(255) NOT NULL,
                          `timestamp` BIGINT NOT NULL,
                          `step` BIGINT NOT NULL,
                          `value` DOUBLE NOT NULL,
                          `tags` json
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE DATABASE log_store
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
#作业2日志表
CREATE TABLE Log (
                      Id INT AUTO_INCREMENT PRIMARY KEY,
                      hostname VARCHAR(255),
                      file VARCHAR(255),
                      content TEXT,
                      create_time DATETIME,
                      update_time DATETIME
);

