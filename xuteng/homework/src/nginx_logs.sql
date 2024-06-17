CREATE TABLE nginx_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    server VARCHAR(50),
    timestamp DATETIME,
    qps INT,
    http_200 INT,
    http_500_plus INT,
    CONSTRAINT uc_server_timestamp UNIQUE (server, timestamp)
);