package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestReadFirstLineFromFile(t *testing.T) {
	file := "linux_cpu_example.txt"
	data, err := readFirstLineFromFile(file)
	assert.Nil(t, err)
	assert.Equal(t, "cpu  2310622 512 2961553 1154598147 4243 0 3471 0 0 0", data)
}

func TestFormatFloat(t *testing.T) {
	num := 0.002553
	assert.Equal(t, 0.0, formatFloat(num, 0))
	assert.Equal(t, 0.00, formatFloat(num, 2))
	assert.Equal(t, 0.00, formatFloat(num, 2))
	assert.Equal(t, 0.0026, formatFloat(num, 4))
	assert.Equal(t, 0.0025530, formatFloat(num, 7))
	assert.Equal(t, 0.00255300, formatFloat(num, 8))
	assert.Equal(t, -0.00255300, formatFloat(-num, 8))
}
