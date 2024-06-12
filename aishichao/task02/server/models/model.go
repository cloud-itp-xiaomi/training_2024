package models

type LogInformation struct {
	Hostname string   `json:"hostname"`
	File     string   `json:"file"`
	Logs     []string `json:"logs"`
}

type LogPost struct {
	LogInformation []LogInformation `json:"logInformation"`
	LogStorage     string           `json:"logs"`
}
