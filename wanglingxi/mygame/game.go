package main

import (
	//"image"
	"fmt"
	"image/color"
	"log"
	"time"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/examples/resources/fonts"
	"github.com/hajimehoshi/ebiten/v2/text"
	"golang.org/x/image/font"
	"golang.org/x/image/font/opentype"
	//"github.com/hajimehoshi/ebiten/text/v2"
	//"github.com/hajimehoshi/ebiten/v2/ebitenutil"
)

type Mode int

// 表示当前游戏状态
const (
	ModeTitle Mode = iota //启动
	ModeGame              //游戏中
	ModeOver              //结束
)

var (
	titleArcadeFont font.Face
	arcadeFont      font.Face
	smallArcadeFont font.Face
)

type Game struct {
	mode  Mode
	input *Input
	cfg   *Config
	shark *Shark
	foods map[*Food]struct{} //管理鱼食

	foodCh    chan *Food //食物通道
	foodCount int
	overMsg   string
}

func NewGame() *Game {
	cfg := loadConfig()
	ebiten.SetWindowSize(cfg.ScreenWidth, cfg.ScreenHeight)
	ebiten.SetWindowTitle(cfg.Title)

	g := &Game{
		input: &Input{},
		shark: NewShark(cfg),
		foods: make(map[*Food]struct{}),
		//foodCh: make(chan *Food),
		cfg: cfg,
	}
	g.foodCh = make(chan *Food) //初始化食物通道
	//fmt.Println("初始化食物通道完成")
	g.init()
	return g
}

func (g *Game) init() {
	//g.CreateFoods()
	//fmt.Println("鱼食制作完成")
	g.CreateFonts()
	g.foodCount = 0
	g.overMsg = ""
}

func (g *Game) CreateFoods() {
	go func() {
		fmt.Println("1生产鱼食中")
		NewFood(g.cfg, g.shark, g.foodCh)
		time.Sleep(5 * time.Second)
		fmt.Println("sleep1")

	}()
	for {

		//food := <-g.foodCh
		g.addFood(<-g.foodCh)
		fmt.Println("2生产鱼食中")

	}
}

/*func (g *Game) CreateFoods() {
	fmt.Println("1生产鱼食中")
	NewFood(g.cfg, g.shark, g.foodCh)

	fmt.Println("finish NewFood")
	time.Sleep(5 * time.Second)
	fmt.Println("sleep")
	food := <-g.foodCh
	fmt.Println("foodCh open") //未输出
	g.addFood(food)
	fmt.Println("2生产鱼食中") //未输出
}*/

func (g *Game) CreateFonts() {
	tt, err := opentype.Parse(fonts.PressStart2P_ttf)
	if err != nil {
		log.Fatal(err)
	}
	const dpi = 72
	titleArcadeFont, err = opentype.NewFace(tt, &opentype.FaceOptions{
		Size:    float64(g.cfg.TitleFontSize),
		DPI:     dpi,
		Hinting: font.HintingFull,
	})
	if err != nil {
		log.Fatal(err)
	}
	arcadeFont, err = opentype.NewFace(tt, &opentype.FaceOptions{
		Size:    float64(g.cfg.FontSize),
		DPI:     dpi,
		Hinting: font.HintingFull,
	})
	if err != nil {
		log.Fatal(err)
	}
	smallArcadeFont, err = opentype.NewFace(tt, &opentype.FaceOptions{
		Size:    float64(g.cfg.SmallFontSize),
		DPI:     dpi,
		Hinting: font.HintingFull,
	})
	if err != nil {
		log.Fatal(err)
	}
}

func (g *Game) CheckCollision() {
	for food := range g.foods {
		if CheckCollision(g.shark, food) {
			g.foodCount++
			delete(g.foods, food)
		}
	}

}

func (g *Game) Update() error {
	switch g.mode {
	case ModeTitle:
		if g.input.IsKeyPressed() {
			g.mode = ModeGame
		}
	case ModeGame:
		for food := range g.foods {
			food.y += food.speedFactor
		}

		g.input.Update(g)

		g.CheckCollision()

		for food := range g.foods {
			if food.outOfScreen(g.cfg) {
				delete(g.foods, food)
				continue
			}
			if CheckCollision(g.shark, food) {
				g.foodCount++
				delete(g.foods, food)
				continue
			}
		}

		if g.foodCount >= 20 {
			g.overMsg = "Sharkbaby Die. Game Over!"
		}
		if len(g.overMsg) > 0 {
			//close(g.foodCh)
			g.mode = ModeOver
			g.foods = make(map[*Food]struct{})
		}
		//g.CreateFoods()
	case ModeOver:
		close(g.foodCh)
		fmt.Println(g.overMsg)
		/*if g.input.IsKeyPressed() {
			g.init()
			g.mode = ModeTitle
		}*/
	}

	return nil
}
func (g *Game) addFood(food *Food) {
	g.foods[food] = struct{}{}
	fmt.Println("鱼食+1")
}

func (g *Game) Draw(screen *ebiten.Image) {
	screen.Fill(g.cfg.BgColor)

	var titleTexts []string
	var texts []string
	switch g.mode {
	case ModeTitle:
		titleTexts = []string{"FEED SHARKBABY"}
		texts = []string{"", "", "", "", "", "", "", "PRESS SPACE KEY", "", "OR LEFT MOUSE"}
	case ModeGame:
		g.shark.Draw(screen)
		for food := range g.foods {
			food.Draw(screen)
		}
	case ModeOver:
		texts = []string{"", g.overMsg}
	}

	for i, l := range titleTexts {
		x := (g.cfg.ScreenWidth - len(l)*g.cfg.TitleFontSize) / 2
		text.Draw(screen, l, titleArcadeFont, x, (i+4)*g.cfg.TitleFontSize, color.White)
	}
	for i, l := range texts {
		x := (g.cfg.ScreenWidth - len(l)*g.cfg.FontSize) / 2
		text.Draw(screen, l, arcadeFont, x, (i+4)*g.cfg.FontSize, color.White)
	}
}

func (g *Game) Layout(outsideWidth, outsideHeight int) (screenWidth, screenHeight int) {
	return g.cfg.ScreenWidth, g.cfg.ScreenHeight
}
