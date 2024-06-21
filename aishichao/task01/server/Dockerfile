#Dockerfile for Server
FROM golang:1.21.10
#FROM golang:1.18
WORKDIR /home/asc/dockerWorkSpace

ENV GOPROXY=https://goproxy.cn

COPY . .

RUN go mod tidy \
  &&go build -o server .

CMD ["./server"]
