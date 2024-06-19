package collectorDataType

type ConfigData struct {
	Files      []string `json:"files"`
	LogStorage string   `json:"log_storage"`
}

type LogInformation struct {
	Hostname string   `json:"hostname"`
	File     string   `json:"file"`
	Logs     []string `json:"logs"`
}

type LogPost struct {
	LogInformation []LogInformation `json:"logInformation"`
	LogStorage     string           `json:"logs"`
}
