CREATE TABLE nginx_stats (  
    id INT AUTO_INCREMENT PRIMARY KEY,  
    timestamp DATETIME NOT NULL,  
    qps FLOAT NOT NULL,  
    http_200 INT NOT NULL,  
    http_500_plus INT NOT NULL  
);