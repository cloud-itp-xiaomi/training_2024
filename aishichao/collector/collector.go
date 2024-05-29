package main

import (
    "bufio"
    "bytes"
    "encoding/json"
    "fmt"
    "log"
    "os"
    "strconv"
    "strings"
    "time"
    "net/http"
)

type MetricData struct{
     Metric      string    `json:"metric"`
     Endpoint    string    `json:"endpoint"`
     Timestamp   int64     `json:"timestamp"`
     Step        int64     `json:"step"`
     Value       float64   `json:"value"`
}

func main() {
     fmt.Println("Starting collector...")
     //fmt.Println("CPU and Memory usage at 1 minute intervals:\n")
     //var prevIdleTime, prevTotalTime uint64

     for {
         data:=collectData()
        // 采集CPU利用率
        

        // 打印CPU和内存利用率
        //if prevTotalTime != 0 {
        //fmt.Printf("CPU usage: %6.3f%%, Memory usage: %6.3f%%\n", cpuUsage, memoryUsage)
        //}
        
       //Data=append(Data, data)
       sendData(data)
       time.Sleep(time.Minute)
     }
}

func getCpuUsage(prevIdleTime, prevTotalTime *uint64) (float64, error) {
     file, err := os.Open("/proc/stat")
     if err != nil {
        return 0, err
     }
     defer file.Close()

     scanner := bufio.NewScanner(file)
     scanner.Scan()
     firstLine := scanner.Text()[5:] // get rid of "cpu " plus 2 spaces

     if err := scanner.Err(); err != nil {
        return 0, err
     }

     split := strings.Fields(firstLine)
     idleTime, _ := strconv.ParseUint(split[3], 10, 64)
     totalTime := uint64(0)
     for _, s := range split {
         u, _ := strconv.ParseUint(s, 10, 64)
         totalTime += u
     }

     var cpuUsage float64
     if *prevTotalTime != 0 {
        deltaIdleTime := idleTime - *prevIdleTime
        deltaTotalTime := totalTime - *prevTotalTime
        cpuUsage = (1.0 - float64(deltaIdleTime)/float64(deltaTotalTime)) * 100.0
     }

     *prevIdleTime = idleTime
     *prevTotalTime = totalTime

     return cpuUsage, nil
}

func getMemoryUsage() (float64, error) {
     file, err := os.Open("/proc/meminfo")
     if err != nil {
        return 0, err
     }
     defer file.Close()

     var totalMem, freeMem uint64
     scanner := bufio.NewScanner(file)

     for scanner.Scan() {
         line := scanner.Text()
         fields := strings.Fields(line)
         if len(fields) < 2 {
            continue
         }

         key := fields[0][:len(fields[0])-1] // Remove trailing ':'
         value, err := strconv.ParseUint(fields[1], 10, 64)
         if err != nil {
            return 0, err
         }

         switch key {
         case "MemTotal":
         totalMem = value
         case "MemFree", "Buffers", "Cached", "SReclaimable":
         freeMem += value
         }
     }

     if err := scanner.Err(); err != nil {
        return 0, err
     }

     usedMem := totalMem - freeMem
     memoryUsage := (float64(usedMem) / float64(totalMem)) * 100.0

     return memoryUsage, nil
}

func getHostName() string{
     hostname,err:=os.Hostname()
     if err!=nil{
         log.Println("Error getting hostname:",err)
         return "unknown"
     }
     return hostname
}
     

func sendData(data []MetricData) {
     jsonData, err := json.Marshal(data)
     if err != nil {
         log.Println("Error marshalling data:", err)
         return
     }

     resp, err := http.Post("http://server:8080/api/metric/upload", "application/json", bytes.NewBuffer(jsonData))
     if err != nil {
         log.Println("Error sending data:", err)
         return
     }
     defer resp.Body.Close()

     if resp.StatusCode != http.StatusOK {
         log.Println("Received non-OK response:", resp.StatusCode)
     }
}

func collectData() []MetricData{
     hostname:=getHostName()
     timestamp:=time.Now().Unix()
     //step:=60
     var prevIdleTime, prevTotalTime uint64
     cpuUsage, err := getCpuUsage(&prevIdleTime, &prevTotalTime)
     if err != nil {
         log.Fatal(err)
         cpuUsage=0
     }

     // 采集内存利用率
     memoryUsage, err := getMemoryUsage()
     if err != nil {
         log.Fatal(err)
         memoryUsage=0
     } 
     
     return []MetricData{
            {
                Metric:    "cpu.used.percent",
                Endpoint:  hostname,
                Timestamp: timestamp,
                Step:      60,
                Value:     cpuUsage,
            },
            {
                Metric:    "mem.used.percent",
                Endpoint:  hostname,
                Timestamp: timestamp,
                Step:      60,
                Value:     memoryUsage,
            },
        }
     
}

