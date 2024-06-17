package Http;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.MetricResponse;
import entity.MetricResponseWrapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MetricHttpClient {

    private final HttpClient client;

    public MetricHttpClient() {
        this.client = HttpClient.newHttpClient();
    }

    public String queryMetrics(String endpoint, String metric, long start_ts, long end_ts) throws IOException, InterruptedException {
        String queryUrl;
        if (metric.isEmpty()) {
            queryUrl = String.format("http://localhost:8080/api/metric/query?endpoint=%s&start_ts=%s&end_ts=%s",
                    endpoint, start_ts, end_ts);
        } else {
            queryUrl = String.format("http://localhost:8080/api/metric/query?endpoint=%s&metric=%s&start_ts=%s&end_ts=%s",
                    endpoint, metric, start_ts, end_ts);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(queryUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            List<MetricResponse> metricResponses = parseMetricResponse(response.body());

            // 创建响应封装对象
            MetricResponseWrapper responseWrapper = new MetricResponseWrapper();
            responseWrapper.setCode(200);
            responseWrapper.setMessage("ok");
            responseWrapper.setData(metricResponses);

            // 使用 Jackson 将对象序列化为 JSON 字符串
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(responseWrapper);
        } else {
            throw new IOException("Error: " + response.body());
        }
    }

    // 解析JSON字符串
    private List<MetricResponse> parseMetricResponse(String responseBody) {
        JSONObject responseObject = new JSONObject(responseBody);
        JSONArray jsonArray = responseObject.getJSONArray("data");
        List<MetricResponse> metricResponseList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            MetricResponse metricResponse = new MetricResponse();
            metricResponse.setMetric(jsonObject.getString("metric"));

            JSONArray valuesArray = jsonObject.getJSONArray("values");
            List<MetricResponse.ValueData> values = new ArrayList<>();

            for (int j = 0; j < valuesArray.length(); j++) {
                JSONObject valueObject = valuesArray.getJSONObject(j);
                MetricResponse.ValueData valueData = new MetricResponse.ValueData();
                valueData.setTimestamp(valueObject.getLong("timestamp"));
                valueData.setValue(valueObject.getDouble("value"));
                values.add(valueData);
            }

            metricResponse.setValues(values);
            metricResponseList.add(metricResponse);
        }

        return metricResponseList;
    }
}
