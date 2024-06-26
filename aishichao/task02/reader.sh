#!/bin/bash

function print_usage() {
    echo "Commands:"
    echo "  query [hostname] [file]   - Query metric data from the server"
}

function query_metric() {
#    if [ -z "$1" ] || [ -z "$2" ]; then
#        echo "hostname and file are required for query command."
#        exit 1
#    fi
#
#    HOSTNAME=$1
#    FILE=$2
#    RESPONSE=$(curl -s "$SERVER_URL?hostname=$HOSTNAME&file=$FILE")
#
#    echo "log data for '$FILE' on '$HOSTNAME':"
#    echo "$RESPONSE" | jq .
    read -p "Enter hostname (or leave empty for all hostname): " HOSTNAME
    read -p "Enter file (or leave empty for all file): " FILE
    QUERY_STRING=""
    if [ -n "$HOSTNAME" ]; then
        QUERY_STRING="${QUERY_STRING}hostname=$HOSTNAME&"
    else
        echo "hostname is not provided. Querying all hostname."
    fi

    if [ -n "$FILE" ]; then
        QUERY_STRING="${QUERY_STRING}file=$FILE&"
    else
        echo "file is not provided. Querying all file."
    fi

     # 去掉最后一个多余的 &
    QUERY_STRING="${QUERY_STRING%&}"

    # 发送请求
    RESPONSE=$(curl -s "$SERVER_URL?$QUERY_STRING")

    echo "log data for '$FILE' on '$HOSTNAME':"
    echo "$RESPONSE" | jq .
}

SERVER_URL="http://localhost:8080/api/log/query"
COMMAND=$1

case $COMMAND in
    query)
        query_metric $2 $3
        ;;
    *)
        print_usage
        exit 1
        ;;
esac
