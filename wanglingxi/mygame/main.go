package main

/*
屏幕顶部随机出现鱼食，玩家左右移动鲨鱼吃鱼食。
规定时间内，没吃到5~10个鱼食的，游戏失败。
MySQL记录玩家信息：id,pwd,name,鲨鱼种类，成长值。
游戏设计登录界面，输入name和pwd进入游戏。
*/

import (
	"log"

	"github.com/hajimehoshi/ebiten/v2"
)

func main() {

	game := NewGame()
	if err := ebiten.RunGame(game); err != nil {
		log.Fatal(err)
	}
}
