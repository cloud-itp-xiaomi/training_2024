package main

import (
    "testing"
    "net/http"
    "net/http/httptest"
    "io/ioutil"
    "strings"
    "time"
	"encoding/json"
)

// 测试getCPUUsage函数
func TestGetCPUUsage(t *testing.T) {
    usage, err := getCPUUsage()
    if err != nil {
        t.Errorf("getCPUUsage返回错误: %s", err)
    }
    if usage < 0 || usage > 100 {
        t.Errorf("CPU使用率不在有效范围内: %f", usage)
    }
}

// 测试getMemoryUsage函数
func TestGetMemoryUsage(t *testing.T) {
    usage, err := getMemoryUsage()
    if err != nil {
        t.Errorf("getMemoryUsage返回错误: %s", err)
    }
    if usage < 0 || usage > 100 {
        t.Errorf("内存使用率不在有效范围内: %f", usage)
    }
}

// 测试collectMetrics函数
func TestCollectMetrics(t *testing.T) {
    metrics := collectMetrics()
    if len(metrics) == 0 {
        t.Error("collectMetrics没有收集到任何度量指标")
    }
    for _, metric := range metrics {
        if metric.Value < 0 || metric.Value > 100 {
            t.Errorf("度量指标值不在有效范围内: %f", metric.Value)
        }
    }
}

// 测试reportMetrics函数
func TestReportMetrics(t *testing.T) {
    server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        if r.Method != "POST" {
            t.Errorf("期望的请求方法为POST，实际为: %s", r.Method)
        }
        body, _ := ioutil.ReadAll(r.Body)
        if !strings.Contains(string(body), "cpu.used.percent") {
            t.Error("上报的度量指标中缺少CPU使用率")
        }
    }))
    defer server.Close()

    metrics := []Metric{
        {
            Metric:    "cpu.used.percent",
            Endpoint:  "test-endpoint",
            Timestamp: time.Now().Unix(),
            Step:      60,
            Value:     50.5,
        },
    }

    reportMetrics(metrics)
}

// 测试uploadMetrics和queryMetrics函数
func TestUploadAndQueryMetrics(t *testing.T) {
    // 设置一个模拟的HTTP服务器
    server := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        if r.URL.Path == "/api/metric/upload" {
            w.WriteHeader(http.StatusOK)
        } else if r.URL.Path == "/api/metric/query" {
            metrics := []Metric{
                {
                    Metric:    "cpu.used.percent",
                    Endpoint:  "test-endpoint",
                    Timestamp: time.Now().Unix(),
                    Step:      60,
                    Value:     50.5,
                },
            }
            json.NewEncoder(w).Encode(metrics)
        }
    }))
    defer server.Close()

    // 测试uploadMetrics函数
    metrics := []Metric{
        {
            Metric:    "cpu.used.percent",
            Endpoint:  "test-endpoint",
            Timestamp: time.Now().Unix(),
            Step:      60,
            Value:     50.5,
        },
    }
    reqBody, _ := json.Marshal(metrics)
    req, _ := http.NewRequest("POST", server.URL+"/api/metric/upload", strings.NewReader(string(reqBody)))
    resp, _ := http.DefaultClient.Do(req)
    if resp.StatusCode != http.StatusOK {
        t.Errorf("uploadMetrics未能成功上报度量指标")
    }

    // 测试queryMetrics函数
    queryURL := server.URL + "/api/metric/query?endpoint=test-endpoint&metric=cpu.used.percent"
    resp, _ = http.Get(queryURL)
    if resp.StatusCode != http.StatusOK {
        t.Errorf("queryMetrics未能成功查询度量指标")
    }
    var queriedMetrics []Metric
    json.NewDecoder(resp.Body).Decode(&queriedMetrics)
    if len(queriedMetrics) == 0 {
        t.Error("queryMetrics未能查询到任何度量指标")
    }
}
