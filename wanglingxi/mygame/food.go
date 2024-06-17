package main

import (
	"fmt"
	"image"
	"math/rand"

	//"sync"
	"time"

	"github.com/hajimehoshi/ebiten/v2"
)

type Food struct {
	GameObject
	image       *ebiten.Image
	speedFactor float64
}

func NewFood(cfg *Config, shark *Shark, foodCh chan<- *Food) {
	rect := image.Rect(0, 0, cfg.FoodWidth, cfg.FoodHeight)
	img := ebiten.NewImageWithOptions(rect, nil)

	img.Fill(cfg.FoodColor)

	rand.Seed(time.Now().UnixNano())

	food := &Food{
		GameObject: GameObject{
			width:  cfg.FoodWidth,
			height: cfg.FoodHeight,
			x:      float64(rand.Intn(500) + 50), //屏幕顶部随机位置掉落鱼食
			y:      0,
		},
		image: img,

		speedFactor: cfg.FoodSpeedFactor,
	}
	fmt.Println("正在制作鱼食...")
	foodCh <- food
	fmt.Println("食物通道已开启")
}

func (food *Food) Draw(screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	op.GeoM.Translate(food.x, food.y)
	screen.DrawImage(food.image, op)
}

func (food *Food) outOfScreen(cfg *Config) bool {
	return food.y > float64(cfg.ScreenHeight)
}
