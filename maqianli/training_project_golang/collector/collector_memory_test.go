package collector

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

// How to calculate system memory usage from /proc/meminfo (like htop) https://stackoverflow.com/questions/41224738/how-to-calculate-system-memory-usage-from-proc-meminfo-like-htop
// Difference Between MemFree and MemAvailable https://stackoverflow.com/questions/30869297/difference-between-memfree-and-memavailable

func TestPickMemValue(t *testing.T) {
	text := "MemTotal:       131437740 kB"
	value := pickMemValue(text)
	assert.Equal(t, int64(131437740), value)

	// invalid format
	text = "131437740 kB"
	value = pickMemValue(text)
	assert.Equal(t, int64(0), value)

	// invalid format
	text = "131437740"
	value = pickMemValue(text)
	assert.Equal(t, int64(0), value)
}

func TestReadMemFields(t *testing.T) {
	filePath := "linux_memory_example.txt"
	mem, err := readMemStat(filePath)
	assert.Nil(t, err)
	assert.Equal(t, int64(131437740), mem.MemTotal)
	assert.Equal(t, int64(120827536), mem.MemFree)
	assert.Equal(t, int64(124360936), mem.MemAvailable)
}

func TestCalculateMemUsage(t *testing.T) {
	filePath := "linux_memory_example.txt"
	mem, err := readMemStat(filePath)
	assert.Nil(t, err)
	assert.Equal(t, 0.080724, mem.UsagePercentage())
}
