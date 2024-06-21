package storageOperate

import (
	"server/serverDataType"
	"strings"
)

func ParseLogLine(line string) []serverDataType.LogInformation {
	var logInfo []serverDataType.LogInformation
	var individualLogIfo serverDataType.LogInformation
	//分割存储文件的每行字符串
	parts := strings.Split(line, ",")
	for _, part := range parts {
		tagAndValue := strings.SplitN(part, ":", 2)
		if len(tagAndValue) != 2 {
			continue
		}
		key := strings.TrimSpace(tagAndValue[0])
		value := strings.TrimSpace(tagAndValue[1])
		switch key {
		case "Hostname":
			individualLogIfo.Hostname = value
		case "File":
			individualLogIfo.File = value
		case "Log":
			individualLogIfo.Logs = append(individualLogIfo.Logs, value)
		}
	}
	logInfo = append(logInfo, individualLogIfo)
	return logInfo
}

// MergeLogs 使用 map 将相同的主机名和文件名的日志信息合并
func MergeLogs(logs []serverDataType.LogInformation) []serverDataType.LogInformation {
	logMap := make(map[string]serverDataType.LogInformation)

	for _, log := range logs {
		key := log.Hostname + ":" + log.File
		if existingLog, exists := logMap[key]; exists {
			existingLog.Logs = append(existingLog.Logs, log.Logs...)
			logMap[key] = existingLog
		} else {
			logMap[key] = log
		}
	}

	var mergedLogs []serverDataType.LogInformation
	for _, log := range logMap {
		mergedLogs = append(mergedLogs, log)
	}

	return mergedLogs
}

func QueryLocalLogs(hostname string, file string, logInfo []serverDataType.LogInformation) []serverDataType.LogInformation {
	if hostname == "" && file == "" {
		return logInfo
	} else if hostname != "" && file == "" {
		if logInfo[0].Hostname == hostname {
			return logInfo
		}
	} else if hostname == "" {
		if logInfo[0].File == file {
			return logInfo
		}
	} else {
		if logInfo[0].Hostname == hostname && logInfo[0].File == file {
			return logInfo
		}
	}
	return logInfo
}
