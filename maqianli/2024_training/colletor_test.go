package main

import "testing"
import "github.com/stretchr/testify/assert"

// https://rosettacode.org/wiki/Linux_CPU_utilization#Go
// To do this:
//  read the first line of   /proc/stat
//  discard the first word of that first line   (it's always cpu)
//  sum all of the times found on that first line to get the total time
//  divide the fourth column ("idle") by the total time, to get the fraction of time spent being idle
//  subtract the previous fraction from 1.0 to get the time spent being   not   idle
//  multiple by   100   to get a percentage

func TestCollectTotalCPUTime(t *testing.T) {
	file := "linux_cpu_example.txt"
	data, err := collectTotalCPUTime(file)
	assert.Nil(t, err)
	assert.Equal(t, 1165507628, data)
}
