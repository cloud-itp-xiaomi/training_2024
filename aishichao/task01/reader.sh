#!/bin/bash

function print_usage() {
    echo "Commands:"
    echo "  query [metric] [endpoint] [start_ts] [end_ts]  - Query metric data from the server"
}

function query_metric() {
#    if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ]; then
#        echo "Metric, endpoint, start_ts, and end_ts are required for query command."
#        exit 1
#    fi
#
#    METRIC=$1
#    ENDPOINT=$2
#    START_TS=$3
#    END_TS=$4
#    RESPONSE=$(curl -s "$SERVER_URL?metric=$METRIC&endpoint=$ENDPOINT&start_ts=$START_TS&end_ts=$END_TS")
#
#    echo "Metric data for '$METRIC' on '$ENDPOINT' from $START_TS to $END_TS:"
#    echo "$RESPONSE" | jq .
    read -p "Enter metric (or leave empty for all metrics): " METRIC
    read -p "Enter endpoint (or leave empty for all endpoints): " ENDPOINT
    read -p "Enter start timestamp (or leave empty for earliest timestamp): " START_TS
    read -p "Enter end timestamp (or leave empty for current timestamp): " END_TS
    QUERY_STRING=""

    if [ -n "$METRIC" ]; then
        QUERY_STRING="${QUERY_STRING}metric=$METRIC&"
    else
        echo "Metric is not provided. Querying all metrics."
    fi

    if [ -n "$ENDPOINT" ]; then
        QUERY_STRING="${QUERY_STRING}endpoint=$ENDPOINT&"
    else
        echo "Endpoint is not provided. Querying all endpoints."
    fi

    if [ -n "$START_TS" ]; then
        QUERY_STRING="${QUERY_STRING}start_ts=$START_TS&"
    else
        echo "Start timestamp is not provided. Using earliest timestamp (0)."
    fi

    if [ -n "$END_TS" ]; then
        QUERY_STRING="${QUERY_STRING}end_ts=$END_TS&"
    else
        echo "End timestamp is not provided. Using current timestamp."
    fi

      # 去掉最后一个多余的 &
      QUERY_STRING="${QUERY_STRING%&}"

      # 发送请求
      RESPONSE=$(curl -s "$SERVER_URL?$QUERY_STRING")

      echo "Metric data for '$METRIC' on '$ENDPOINT' from $START_TS to $END_TS:"
      echo "$RESPONSE" | jq .
}

SERVER_URL="http://localhost:8080/api/metric/query"
COMMAND=$1

case $COMMAND in
    query)
        query_metric
        ;;
    *)
        print_usage
        exit 1
        ;;
esac
