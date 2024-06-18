package service

type Utilization struct {
	Metric      string
	Endpoint    string
	CollectTime int64
	Value       float64
}

type Log struct {
	Hostname           string
	File               string
	Logs               []string
	FileLastUpdateTime string
}
