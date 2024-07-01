-- # MySQL
-- ## 创建新的数据库
CREATE DATABASE `jiu_sys_monitor` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


-- ## 创建信息捕获表

# CREATE TABLE `SysInfoCapture` (
#                            `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
#                            `metric` VARCHAR(255) NOT NULL,
#                            `endpoint` VARCHAR(255) NOT NULL,
#                            `timestamp` BIGINT NOT NULL,
#                            `step` BIGINT NOT NULL,
#                            `value` DOUBLE NOT NULL,
#                            `tags` json
# );

CREATE TABLE `sys_info_capture` (
                                  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  `metric` VARCHAR(255) NOT NULL,
                                  `endpoint` VARCHAR(255) NOT NULL,
                                  `timestamp` BIGINT NOT NULL,
                                  `step` BIGINT NOT NULL,
                                  `value` DOUBLE NOT NULL,
                                  `tags` json
);

CREATE TABLE log_infos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           hostname VARCHAR(255),
                           file VARCHAR(255)
);

CREATE TABLE log_messages (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              log_info_id BIGINT,
                              message TEXT,
                              FOREIGN KEY (log_info_id) REFERENCES log_infos(id)
);
