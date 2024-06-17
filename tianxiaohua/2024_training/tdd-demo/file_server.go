package tdd_demo

import (
	"bufio"
	"fmt"
	"os"
	"strings"
)

type FileServer struct {
	Server
	fileName string
}

func (s *FileServer) saveLog(log Log) error {
	file, err := os.Create(s.fileName)
	if err != nil {
		return err
	}
	defer file.Close()

	writer := bufio.NewWriter(file)
	lines := serializeLog(log)
	for _, line := range lines {
		_, err := writer.WriteString(line + "\n")
		if err != nil {
			return err
		}
	}

	err = writer.Flush()
	if err != nil {
		return err
	}
	return nil
}

func (s *FileServer) readLog() Log {
	file, err := os.Open(s.fileName)
	if err != nil {
		return Log{}
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	if err := scanner.Err(); err != nil {
		return Log{}
	}

	return deserializeLog(lines)
}

func (s *FileServer) saveLogs(logs []Log) error {
	file, err := os.Create(s.fileName)
	if err != nil {
		return err
	}
	defer file.Close()

	writer := bufio.NewWriter(file)
	for i := 0; i < len(logs); i++ {
		lines := serializeLog(logs[i])
		for _, line := range lines {
			_, err := writer.WriteString(line + "\n")
			if err != nil {
				return err
			}
		}

		err = writer.Flush()
		if err != nil {
			return err
		}
	}
	return nil
}

func (s *FileServer) readLogs() []Log {
	file, err := os.Open(s.fileName)
	if err != nil {
		return []Log{}
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := scanner.Text()
		lines = append(lines, line)
	}
	if err := scanner.Err(); err != nil {
		return []Log{}
	}

	return deserializeLogs(lines)
}

func deserializeLog(lines []string) Log {
	var hostname string
	var file string
	var logs []string
	for _, line := range lines {
		parts := strings.Split(line, "|")
		if len(parts) != 3 {
			continue
		}
		hostname = parts[0]
		file = parts[1]
		logs = append(logs, parts[2])
	}

	return Log{
		Hostname: hostname,
		File:     file,
		Logs:     logs,
	}
}

func deserializeLogs(lines []string) []Log {
	var log_datas []Log
	var hostname string
	var file string
	var logs []string
	for _, line := range lines {
		parts := strings.Split(line, "|")
		if len(parts) != 3 {
			continue
		}
		hostname = parts[0]
		file = parts[1]
		logs = append(logs, parts[2])
		temp_log := Log{
			Hostname: hostname,
			File:     file,
			Logs:     logs,
		}
		l := len(log_datas)
		if l == 0 {
			log_datas = append(log_datas, temp_log)
		} else {
			if log_datas[len(log_datas)-1].Hostname == hostname && log_datas[len(log_datas)-1].File == file {
				log_datas[len(log_datas)-1] = temp_log
			} else {
				logs = []string{}
				logs = append(logs, parts[2])
				log_datas = append(log_datas, temp_log)
			}
		}

	}
	return log_datas
}

func serializeLog(log Log) []string {
	var lines []string
	for _, line := range log.Logs {
		lines = append(lines, fmt.Sprintf("%s|%s|%s", log.Hostname, log.File, line))
	}
	return lines
}
