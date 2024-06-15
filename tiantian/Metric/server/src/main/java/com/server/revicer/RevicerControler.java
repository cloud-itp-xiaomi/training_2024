package com.server.revicer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.server.entity.Metric;
import com.server.service.RevicerService;


@RequestMapping("/api/metric/")
@Controller
public class RevicerControler {

	@Autowired
	private RevicerService rService;
	
	@ResponseBody
	@PostMapping("upload")
	public Map<String, Object> upload(@RequestBody String res) {
		Map<String, Object> result = new HashMap<>();
		try {
			rService.saveMetric(res);
			result.put("code", 1);
			result.put("message", "ok");
			return result;
		} catch (Exception e) {
			result.put("code", 0);
			result.put("message", e.getMessage());
		}
		return result;
	}
	

	@ResponseBody
	@GetMapping("query")
	public Map<String, Object> query(HttpServletRequest request) {
		Map<String, Object> mResult = new HashMap<>();
		try {
			String endpoint = request.getParameter("endpoint");
			if(StringUtils.isEmpty(endpoint)) {
				throw new Exception("机器编号不能为空！");
			}
			String metric = request.getParameter("metric");
			String start_ts = request.getParameter("start_ts");
			if(StringUtils.isEmpty(start_ts)) {
				throw new Exception("开始时间不能为空！");
			}
			String end_ts = request.getParameter("end_ts");
//			if(StringUtils.isEmpty(end_ts)) {
//				throw new Exception("结束时间不能为空！");
//			}
//			List listCpu = new ArrayList();
//			List listMem = new ArrayList();
			List<Map<String, Object>> listCpu = new ArrayList<>();
			List<Map<String, Object>> listMem = new ArrayList<>();
			List<Metric> list = rService.getMetric(endpoint, metric, Integer.valueOf(start_ts), Integer.valueOf(end_ts));

			for (Metric m : list) {
				Map<String, Object> mres = new HashMap<>();
				mres.put("timestamp", m.getTimestamp());
				mres.put("value", m.getValue());
				if ("cpu.used.percent".equals(m.getMetric())) {
					listCpu.add(mres);
				} else {
					listMem.add(mres);
				}
			}
			List<Map<String, Object>> data = new ArrayList<>();
			Map<String, Object> mData1 = new HashMap<>();
			mData1.put("metric", "cpu.used.percent");
			mData1.put("values", listCpu);

			Map<String, Object> mData2 = new HashMap<>();
			mData2.put("metric", "mem.used.percent");
			mData2.put("values", listMem);

			data.add(mData1);
			data.add(mData2);

			mResult.put("code", 1);
			mResult.put("message", "ok");
			mResult.put("data", data);
			return mResult;
		}catch (Exception e) {
			mResult.put("code", 0);
			mResult.put("message", e.getMessage());
		}
		return mResult;
	}
}
