#Dockerfile for Collector
FROM golang:1.21.10

WORKDIR /home/asc/dockerWorkSpace

ENV GOPROXY=https://goproxy.cn

COPY . .

RUN go mod tidy \
    &&go build -o collector .

CMD ["./collector"]



