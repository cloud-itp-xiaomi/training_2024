package main

import (
	"encoding/json"
	"image/color"
	"log"
	"os"
)

type Config struct {
	ScreenWidth      int        `json:"screenWidth"`
	ScreenHeight     int        `json:"screenHeight"`
	Title            string     `json:"title"`
	BgColor          color.RGBA `json:"bgColor"`
	SharkSpeedFactor float64    `json:"sharkSpeedFactor"`
	FoodColor        color.RGBA `json:"foodColor"`
	FoodWidth        int        `json:"foodWidth"`
	FoodHeight       int        `json:"foodHeight"`
	FoodSpeedFactor  float64    `json:"foodSpeedFactor"`
	MaxFoodNum       int        `json:"maxFoodNum"`
	FoodInterval     int64      `json:"foodInterval"`
	TitleFontSize    int        `json:"titleFontSize"`
	FontSize         int        `json:"fontSize"`
	SmallFontSize    int        `json:"smallFontSize"`
}

func loadConfig() *Config {
	f, err := os.Open("./config.json")
	if err != nil {
		log.Fatalf("os.Open failed: %v\n", err)
	}

	var cfg Config
	err = json.NewDecoder(f).Decode(&cfg)
	if err != nil {
		log.Fatalf("json.Decode failed: %v\n", err)
	}

	return &cfg
}
