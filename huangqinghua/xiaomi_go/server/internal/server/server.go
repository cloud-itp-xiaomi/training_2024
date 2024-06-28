package server

import "server/config"

type Server struct {
	config *config.Config
}

func NewServer(cfg *config.Config) *Server {
	return &Server{
		config: cfg,
	}
}
