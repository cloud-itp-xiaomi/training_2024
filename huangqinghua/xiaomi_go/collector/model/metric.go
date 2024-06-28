package model

type Metric struct {
	Metric    string
	Endpoint  string
	Timestamp int64
	Step      int64
	Value     float64
}
