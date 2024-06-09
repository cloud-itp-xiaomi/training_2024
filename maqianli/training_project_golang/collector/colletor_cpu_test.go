package collector

import (
	"testing"
)
import "github.com/stretchr/testify/assert"

// https://rosettacode.org/wiki/Linux_CPU_utilization#Go
// To do this:
//  read the first line of   /proc/stat
//  discard the first word of that first line   (it's always cpu)
//  sum all of the times found on that first line to get the total time
//  divide the fourth column ("idle") by the total time, to get the fraction of time spent being idle
//  subtract the previous fraction from 1.0 to get the time spent being   not   idle
//  multiple by   100   to get a percentage

func TestCollectCPUUsageFromFile(t *testing.T) {
	file := "linux_cpu_example.txt"
	data, err := collectCPUUsageFromFile(file)
	assert.Nil(t, err)
	assert.Equal(t, 0.004553, data)
}

func TestParseCPUUsageText(t *testing.T) {
	text := "cpu  2310622 512 2961553 1154598147 4243 0 3471 0 0 0"
	data, err := parseCPUUsageText(text)
	assert.Nil(t, err)
	expect := []int{2310622, 512, 2961553, 1154598147, 4243, 0, 3471, 0, 0, 0}
	assert.Equal(t, expect, data)
}

func TestCollectCPUTotalTime(t *testing.T) {
	data := []int{2310622, 512, 2961553, 1154598147, 4243, 0, 3471, 0, 0, 0}
	got, err := collectTotalCPUTime(data)
	assert.Nil(t, err)
	assert.Equal(t, 1159878548, got)
}

func TestCollectCPUIdleTime(t *testing.T) {
	data := []int{2310622, 512, 2961553, 1154598147, 4243, 0, 3471, 0, 0, 0}
	got, err := collectIdleCPUTime(data)
	assert.Nil(t, err)
	assert.Equal(t, 1154598147, got)
}

func TestCalculateCPUIdlePercentage(t *testing.T) {
	data := []int{2310622, 512, 2961553, 1154598147, 4243, 0, 3471, 0, 0, 0}
	idle, _ := collectIdleCPUTime(data)
	total, _ := collectTotalCPUTime(data)
	got, err := calculateCPUIdlePercentage(idle, total)
	assert.Nil(t, err)
	assert.Equal(t, 0.995447, got)
}
