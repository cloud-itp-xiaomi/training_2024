package config

import (
	"github.com/spf13/viper"
)

type Config struct {
	MySQL MySQLConfig `yaml:"mysql"`
	Port  int         `yaml:"port"`
}

func LoadConfig() (*Config, error) {
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")
	viper.AddConfigPath(".")
	viper.AddConfigPath("config") // 添加配置文件路径

	// 文件读取失败
	if err := viper.ReadInConfig(); err != nil {
		return nil, err
	}

	var cfg Config
	if err := viper.Unmarshal(&cfg); err != nil {
		return nil, err
	}

	return &cfg, nil
}
