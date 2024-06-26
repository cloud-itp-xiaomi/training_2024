package org.example.work2.Controller;

import org.example.work2.pojo.Cpu;
import org.example.work2.pojo.MetricDataOutput;
import org.example.work2.pojo.Result;
import org.example.work2.pojo.ValueEntry;
import org.example.work2.service.CupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private CupService cupService;

    @PostMapping("/api/metric/upload")
    public Result function(@RequestBody Cpu[] cpus){

        for (Cpu cpu : cpus) {
            String metric = cpu.getMetric();
            String endpoint = cpu.getEndpoint();
            String step = cpu.getStep();
            String timestamp = cpu.getTimestamp();
            String value = cpu.getValue();
            if(metric != "" && endpoint != "" &&  step != "" && timestamp  != ""&& value != ""){
                cupService.add(cpu);
            }else {
                Result.error("参数不能为空");
            }
        }
        return Result.success("ok");
    }

    @GetMapping("/api/metric/query")
    public Result query(@RequestParam String metric,@RequestParam String endpoint,@RequestParam int start_ts,@RequestParam int end_ts){
        List<Cpu> query = cupService.query(metric, endpoint, start_ts, end_ts);
        ArrayList<ValueEntry> valueEntries = new ArrayList<>();
        ValueEntry valueEntry = new ValueEntry();
        for (Cpu cpu : query) {
            valueEntry.setValue(cpu.getValue());
            valueEntry.setTimestamp(cpu.getTimestamp());
            valueEntries.add(valueEntry);
        }
        MetricDataOutput metricDataOutput = new MetricDataOutput();
        metricDataOutput.setMetric(metric);
        metricDataOutput.setValues(valueEntries);
        return Result.success(metricDataOutput);
    }


}
