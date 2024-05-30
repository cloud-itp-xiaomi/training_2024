#Dockerfile for Server
FROM golang:1.21.10

WORKDIR /home/asc/dockerWorkSpace

ENV GOPROXY=https://goproxy.cn

COPY . .
#apk add --no-cache git \
#RUN go mod tidy \
RUN go build -o server .

CMD ["./server"]
