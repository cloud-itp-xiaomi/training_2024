CREATE TABLE `Metrics` (
                          `id` INT AUTO_INCREMENT PRIMARY KEY,
                          `metric` VARCHAR(255) NOT NULL,
                          `endpoint` VARCHAR(255) NOT NULL,
                          `timestamp` BIGINT NOT NULL,
                          `step` BIGINT NOT NULL,
                          `value` DOUBLE NOT NULL,
                          `tags` json
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;