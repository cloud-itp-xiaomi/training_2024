package main

import (
	"log"
	//"time"

	"github.com/hajimehoshi/ebiten/v2"
	"github.com/hajimehoshi/ebiten/v2/ebitenutil"
	_ "golang.org/x/image/bmp"
)

type Shark struct {
	GameObject
	image       *ebiten.Image
	speedFactor float64
}

func NewShark(cfg *Config) *Shark {
	img, _, err := ebitenutil.NewImageFromFile("E:\\gamesource\\shark1.bmp")
	if err != nil {
		log.Fatal(err)
	}

	bounds := img.Bounds()

	width, height := bounds.Dx(), bounds.Dy()
	return &Shark{
		GameObject: GameObject{

			width:  width,
			height: height,
			x:      float64(cfg.ScreenWidth-width) / 2,
			y:      float64(cfg.ScreenHeight - height),
		},
		image:       img,
		speedFactor: cfg.SharkSpeedFactor,
	}
}

func (shark *Shark) Draw(screen *ebiten.Image) {
	op := &ebiten.DrawImageOptions{}
	op.GeoM.Translate(shark.x, shark.y)
	screen.DrawImage(shark.image, op)

}
