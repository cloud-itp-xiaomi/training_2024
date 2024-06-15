package entity;

import java.util.List;

public class MetricResponse {
    private String metric;
    private List<ValueData> values;

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public List<ValueData> getValues() {
        return values;
    }

    public void setValues(List<ValueData> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "entity.MetricResponse{" +
                "metric='" + metric + '\'' +
                ", values=" + values +
                '}';
    }

    public static class ValueData {
        private long timestamp;
        private double value;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "ValueData{" +
                    "timestamp=" + timestamp +
                    ", value=" + value +
                    '}';
        }
    }
}
