<script setup lang="ts">
import axios from "axios";
import {onMounted, ref} from "vue";

const endpoint = ref('')
const metric = ref('cpu.used.percent')
const start_ts = ref('')
const end_ts = ref('')
const endpoint_list = ref([])
const cpu_current_page = ref(1)
const cpu_page_num = ref(10)
const mem_current_page = ref(1)
const mem_page_num = ref(10)
const page_size = 10
const cpu_utilization_data = ref([])
const mem_utilization_data = ref([])

function get_endpoint() {
  axios.get('http://localhost:8080/api/endpoint/query').then(res => {
    endpoint_list.value = res.data.data
    console.log(endpoint_list.value)
  })
}

function get_file() {
  axios.get('http://localhost:8080/api/file/query').then(res => {
    log_name_list.value = res.data.data
  })
}

onMounted(() => {
  get_endpoint()
  get_file()
})

function utilization_query() {
  if (endpoint.value === '') {
    alert('请选择一个主机名称！')
    return
  }
  const start = new Date(start_ts.value).getTime() / 1000;
  const end = new Date(end_ts.value).getTime() / 1000;
  if (start > end) {
    alert('开始时间不能大于结束时间')
    return
  }
  cpu_current_page.value = 1
  mem_current_page.value = 1
  cpu_page_num.value = 10
  mem_page_num.value = 10
  axios.get('http://localhost:8080/api/metric/query?', {
    params: {
      endpoint: endpoint.value,
      metric: metric.value,
      start_ts: start,
      end_ts: end,
      current_page: 1,
    }
  }).then(res => {
    cpu_utilization_data.value = []
    mem_utilization_data.value = []
    console.log(res.data.data)
    for (let i = 0; i < res.data.data.length; i++) {
      for (let j = 0; j < res.data.data[i].values.length; j++) {
        if (res.data.data[i].metric === 'cpu.used.percent') {
          const time = new Date(res.data.data[i].values[j].timestamp * 1000).toLocaleString();
          cpu_utilization_data.value.push({
            metric: res.data.data[i].metric,
            timestamp: time,
            value: res.data.data[i].values[j].value
          });
          cpu_page_num.value = res.data.data[i].totalNum
        }
        if (res.data.data[i].metric === 'mem.used.percent') {
          const time = new Date(res.data.data[i].values[j].timestamp * 1000).toLocaleString();
          mem_utilization_data.value.push({
            metric: res.data.data[i].metric,
            timestamp: time,
            value: res.data.data[i].values[j].value
          });
          mem_page_num.value = res.data.data[i].totalNum
        }
      }
    }
    console.log(mem_current_page.value)
  })
}

const handleCpuCurrentChange = (val: number) => {
  console.log(`${val} items per page`)
  const start = new Date(start_ts.value).getTime() / 1000;
  const end = new Date(end_ts.value).getTime() / 1000;
  axios.get('http://localhost:8080/api/metric/query?', {
    params: {
      endpoint: endpoint.value,
      metric: metric.value,
      start_ts: start,
      end_ts: end,
      current_page: val,
    }
  }).then(res => {
    cpu_utilization_data.value = []
    for (let i = 0; i < res.data.data.length; i++) {
      for (let j = 0; j < res.data.data[i].values.length; j++) {
        if (res.data.data[i].metric === 'cpu.used.percent') {
          const time = new Date(res.data.data[i].values[j].timestamp * 1000).toLocaleString();
          cpu_utilization_data.value.push({
            metric: res.data.data[i].metric,
            timestamp: time,
            value: res.data.data[i].values[j].value
          });
          cpu_page_num.value = res.data.data[i].totalNum
        }
      }
    }
  })
}

const handleMemCurrentChange = (val: number) => {
  console.log(`${val} items per page`)
  const start = new Date(start_ts.value).getTime() / 1000;
  const end = new Date(end_ts.value).getTime() / 1000;
  axios.get('http://localhost:8080/api/metric/query?', {
    params: {
      endpoint: endpoint.value,
      metric: metric.value,
      start_ts: start,
      end_ts: end,
      current_page: val,
    }
  }).then(res => {
    mem_utilization_data.value = []
    for (let i = 0; i < res.data.data.length; i++) {
      for (let j = 0; j < res.data.data[i].values.length; j++) {
        if (res.data.data[i].metric === 'mem.used.percent') {
          const time = new Date(res.data.data[i].values[j].timestamp * 1000).toLocaleString();
          mem_utilization_data.value.push({
            metric: res.data.data[i].metric,
            timestamp: time,
            value: res.data.data[i].values[j].value
          });
          mem_page_num.value = res.data.data[i].totalNum
        }
      }
    }
  })
}

const log_storage_type = ref('')
const log_storage_list = ['es存储', 'MySQL存储']
const log_endpoint = ref('')
const log_choose = ref('')
const log_name_list = ref([])
const log_data = ref([])
const log_current_page = ref(1)
const log_page_num = ref(0)

const handleLogCurrentChange = (val: number) => {
}

function queryLog(){
  axios.get('http://localhost:8080/api/log/query?', {
    params: {
      hostname: log_endpoint.value,
      file: log_choose.value,
      current_page: log_current_page.value,
    }
  }).then(res => {
    log_data.value = []
    log_data.value = res.data.data.logs
    log_page_num.value = res.data.data.totalNum
  })
}
</script>

<template>
  <div class="outer">
    <div>
      <el-tabs class="tab" type="border-card">
        <el-tab-pane label="主机利用率采集系统">
          <div style="display: flex;justify-content: space-between">
            <div class="condition">
              <div class="item">
                <el-select
                    v-model="endpoint"
                    placeholder="主机名"
                    style="width: 240px"
                >
                  <el-option
                      v-for="item in endpoint_list"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </div>
              <div class="item">
                <el-radio-group v-model="metric" class="vertical-radio-group">
                  <el-radio value="cpu.used.percent" size="large">主机CPU利用率</el-radio>
                  <el-radio value="mem.used.percent" size="large">主机内存利用率</el-radio>
                  <el-radio value="" size="large">all</el-radio>
                </el-radio-group>
              </div>
              <div class="item">
                <el-date-picker type="datetime" v-model="start_ts" placeholder="开始时间"/>
              </div>
              <div class="item">
                <el-date-picker type="datetime" v-model="end_ts" placeholder="结束时间"/>
              </div>
              <div class="item">
                <el-button @click="utilization_query">查询</el-button>
              </div>
            </div>
            <div>
              <h3 style="text-align: center">cpu利用率</h3>
              <el-table :data="cpu_utilization_data" style="width: 400px;height: 45vh;border: 1px solid black">
                <el-table-column label="利用率" prop="value"/>
                <el-table-column label="时间" prop="timestamp"/>
              </el-table>
              <div style="display: flex;justify-content: center;padding-top: 1rem">
                <el-pagination
                    v-model="cpu_current_page"
                    :pagesize="page_size"
                    small
                    background
                    layout="prev, pager, next"
                    :total="cpu_page_num"
                    @current-change="handleCpuCurrentChange"
                />
              </div>
            </div>
            <div>
              <h3 style="text-align: center">内存利用率</h3>
              <el-table :data="mem_utilization_data" style="width: 400px;height: 45vh; border: 1px solid black">
                <el-table-column label="利用率" prop="value"/>
                <el-table-column label="时间" prop="timestamp"/>
              </el-table>
              <div style="display: flex;justify-content: center;padding-top: 1rem">
                <el-pagination
                    v-model="mem_current_page"
                    small
                    background
                    layout="prev, pager, next"
                    :total="mem_page_num"
                    @current-change="handleMemCurrentChange"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="主机日志采集系统">
          <div style="display: flex;justify-content: space-between">
            <div class="condition">
              <div class="item">
                <el-select
                    v-model="log_endpoint"
                    placeholder="主机名"
                    style="width: 240px"
                >
                  <el-option
                      v-for="item in endpoint_list"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </div>
              <div class="item">
                <el-select
                    v-model="log_choose"
                    placeholder="日志选择"
                    style="width: 240px"
                >
                  <el-option
                      v-for="item in log_name_list"
                      :key="item"
                      :label="item"
                      :value="item"
                  />
                </el-select>
              </div>
              <div class="item">
                <el-button type="primary" @click="queryLog">查询</el-button>
              </div>
            </div>
            <div>
              <h3 style="text-align: center">日志内容</h3>
              <div  style="width: 800px;height: 45vh; border: 1px solid black">
                <p v-for="item in log_data" style="padding-top: 2px">{{item}}</p>
              </div>
              <div style="display: flex;justify-content: center;padding-top: 1rem">
                <el-pagination
                    v-model="log_current_page"
                    small
                    background
                    layout="prev, pager, next"
                    :total="log_page_num"
                    @current-change="handleLogCurrentChange"
                />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

  </div>
</template>

<style scoped>
.tab {
  height: 60vh;
  width: 1300px;
}

.condition {
  width: 300px;
}

.outer {
  padding: 5rem;
}

.item {
  padding-top: 1rem;
}

.vertical-radio-group {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>

