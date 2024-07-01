package serverDataType

type LogInformation struct {
	Hostname string   `json:"hostname"`
	File     string   `json:"file"`
	Logs     []string `json:"logs"`
}

type LogPost struct {
	LogInformation []LogInformation `json:"logInformation"`
	LogStorage     string           `json:"logs"`
}

type Response struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

type QueryResponse struct {
	Code    int              `json:"code"`
	Message string           `json:"message"`
	Data    []LogInformation `json:"data"`
}
