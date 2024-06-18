user www-data;
worker_processes auto;
pid /run/nginx.pid;
#include /etc/nginx/modules-enabled/*.conf;

events {
    worker_connections 1024;
}
http {
      include mime.types;
      default_type text/html;
      sendfile on;
      keepalive_timeout 65;

      server {
        listen 80;
        root /var/www/html;
        location /hello.html {
           index hello.html hello.htm;
        }
        location /mi.html  {
           index mi.html mi.htm;
       }
    }
}