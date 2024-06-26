package service

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

type FileServer struct {
	LogServer
	fileName string
}

func (s *FileServer) saveLog(log Log) error {
	file, err := os.OpenFile(s.fileName, os.O_APPEND|os.O_WRONLY|os.O_CREATE, 0644)
	if err != nil {
		return err
	}
	defer file.Close()

	writer := bufio.NewWriter(file)
	lines := serializeLog(log)

	// 读取文件内容
	fileInfo, err := file.Stat()
	if err != nil {
		return err
	}

	// 将Log信息插入到文件末尾
	if fileInfo.Size() > 0 {
		_, err = writer.WriteString("\n") // 文件不为空时在新行插入Log
		if err != nil {
			return err
		}
	}

	for _, line := range lines {
		fmt.Println(line)
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

func (s *FileServer) readLog(hostname string, file string) Log {
	logs := s.readLogs()
	for _, log := range logs {
		if log.Hostname == hostname && log.File == file {
			return log
		}
	}
	return Log{}
}

func (s *FileServer) updateLog(log Log) error {
	logs := s.readLogs()
	for i := 0; i < len(logs); i++ {
		log1 := logs[i]
		if log1.Hostname == log.Hostname && log1.File == log.File {
			logs = append(logs[:i], log)
			break
		}
	}
	s.saveLogs(logs)
	return nil
}

func (s *FileServer) getLastUpdateTime(hostname string, file string) int64 {
	logs := s.readLogs()
	for _, log := range logs {
		if log.Hostname == hostname && log.File == file {
			return log.FileLastUpdateTime
		}
	}
	return 0
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
	var last int64
	for _, line := range lines {
		parts := strings.Split(line, "|")
		if len(parts) != 4 {
			continue
		}
		hostname = parts[0]
		file = parts[1]
		logs = append(logs, parts[2])
		last, _ = strconv.ParseInt(parts[3], 10, 64)
		temp_log := Log{
			Hostname:           hostname,
			File:               file,
			Logs:               logs,
			FileLastUpdateTime: last,
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
		lines = append(lines, fmt.Sprintf("%s|%s|%s|%d", log.Hostname, log.File, line, log.FileLastUpdateTime))
	}
	return lines
}
