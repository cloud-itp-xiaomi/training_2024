FROM golang:1.22.4

# 为我们的镜像设置必要的环境变量
ENV GO111MODULE=on \
    GOPROXY=https://goproxy.cn,direct \
    CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64

WORKDIR /project/go-docker/

# COPY go.mod, go.sum and download the dependencies
COPY go.* ./

COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

RUN go mod download

# COPY All things inside the project and build
COPY . .
RUN go build -o /project/go-docker/server .

# 使用 wait-for-it.sh 脚本作为 ENTRYPOINT 确保 mysql 服务可用后再启动应用程序
ENTRYPOINT [ "/wait-for-it.sh", "mysql:3306", "--", "/project/go-docker/server" ]
