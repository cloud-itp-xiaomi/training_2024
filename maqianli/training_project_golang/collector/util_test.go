package collector

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestReadLinesFromFile(t *testing.T) {
	file := "linux_cpu_example.txt"
	data, err := readLinesFromFile(file, 1)
	assert.Nil(t, err)
	assert.Equal(t, 1, len(data))
	assert.Equal(t, "cpu  2310622 512 2961553 1154598147 4243 0 3471 0 0 0", data[0])

	// count is equal to the actual number of lines in the file
	data, err = readLinesFromFile(file, 82)
	assert.Nil(t, err)
	assert.Equal(t, 82, len(data))
	assert.Equal(t, "softirq 667919954 1 350327885 58709 9492910 1026987 0 4547978 112988648 0 189476836", data[81])

	// count is greater than the actual number of lines in the file
	data, err = readLinesFromFile(file, 100)
	assert.NotNil(t, err)

	// file does not exist
	data, err = readLinesFromFile("non_file", 1)
	assert.NotNil(t, err)
	assert.Equal(t, 0, len(data))
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
