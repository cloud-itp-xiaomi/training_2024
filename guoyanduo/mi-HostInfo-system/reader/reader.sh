#!/bin/bash

ENDPOINT=$1
METRIC=$2
START_TS=$3
END_TS=$4

if [ -z "$METRIC" ]; then
  RESPONSE=$(curl -s "http://localhost:8080/api/metric/query?endpoint=$ENDPOINT&start_ts=$START_TS&end_ts=$END_TS")
else
  RESPONSE=$(curl -s "http://localhost:8080/api/metric/query?endpoint=$ENDPOINT&metric=$METRIC&start_ts=$START_TS&end_ts=$END_TS")
fi

echo $RESPONSE | jq .
