#!/bin/bash

function print_usage() {
    echo "Commands:"
    echo "  query [metric] [endpoint] [start_ts] [end_ts]  - Query metric data from the server"
}

function query_metric() {
    if [ -z "$1" ] || [ -z "$2" ] || [ -z "$3" ] || [ -z "$4" ]; then
        echo "Metric, endpoint, start_ts, and end_ts are required for query command."
        exit 1
    fi

    METRIC=$1
    ENDPOINT=$2
    START_TS=$3
    END_TS=$4
    RESPONSE=$(curl -s "$SERVER_URL?metric=$METRIC&endpoint=$ENDPOINT&start_ts=$START_TS&end_ts=$END_TS")

    echo "Metric data for '$METRIC' on '$ENDPOINT' from $START_TS to $END_TS:"
    echo "$RESPONSE" | jq .
}

SERVER_URL="http://localhost:8080/api/metric/query"
COMMAND=$1

case $COMMAND in
    query)
        query_metric $2 $3 $4 $5
        ;;
    *)
        print_usage
        exit 1
        ;;
esac
