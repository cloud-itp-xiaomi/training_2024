package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

// How to calculate system memory usage from /proc/meminfo (like htop) https://stackoverflow.com/questions/41224738/how-to-calculate-system-memory-usage-from-proc-meminfo-like-htop
// Difference Between MemFree and MemAvailable https://stackoverflow.com/questions/30869297/difference-between-memfree-and-memavailable

func TestReadMemTotal(t *testing.T) {
	filePath := "linux_memory_example.txt"
	total, err := readMemTotal(filePath)
	assert.Nil(t, err)
	assert.Equal(t, int64(131437740), total)
}

func TestPickMemValue(t *testing.T) {
	text := "MemTotal:       131437740 kB"
	value, err := pickMemValue(text)
	assert.Nil(t, err)
	assert.Equal(t, int64(131437740), value)
}
