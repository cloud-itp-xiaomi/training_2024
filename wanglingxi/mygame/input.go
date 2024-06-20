package main

import (
	"fmt"
	"time"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/inpututil"
)

type Input struct {
	lastFoodTime time.Time
}

func (i *Input) Update(g *Game) {
	if ebiten.IsKeyPressed(ebiten.KeyLeft) {
		g.shark.x -= g.cfg.SharkSpeedFactor
		if g.shark.x < -float64(g.shark.width)/2 {
			g.shark.x = -float64(g.shark.width) / 2
		}
	} else if ebiten.IsKeyPressed(ebiten.KeyRight) {
		g.shark.x += g.cfg.SharkSpeedFactor
		if g.shark.x > float64(g.cfg.ScreenWidth)-float64(g.shark.width)/2 {
			g.shark.x = float64(g.cfg.ScreenWidth) - float64(g.shark.width)/2
		}
	}

	if ebiten.IsKeyPressed(ebiten.KeySpace) {
		//fmt.Println("0空格键")
		//fmt.Println("food num:", len(g.foods))
		//fmt.Println("max num:", g.cfg.MaxFoodNum)
		//fmt.Println("interval time:", time.Since(i.lastFoodTime).Milliseconds())
		if len(g.foods) < g.cfg.MaxFoodNum && time.Since(i.lastFoodTime).Milliseconds() > g.cfg.FoodInterval {
			fmt.Println("1空格键")
			//NewFood(g.cfg, g.shark, g.foodCh)
			g.CreateFoods()
			fmt.Println("2空格键")
			i.lastFoodTime = time.Now()
		}
	}
}

func (i *Input) IsKeyPressed() bool {
	if inpututil.IsKeyJustPressed(ebiten.KeySpace) {
		return true
	}

	if inpututil.IsMouseButtonJustPressed(ebiten.MouseButtonLeft) {
		return true
	}

	return false
}
