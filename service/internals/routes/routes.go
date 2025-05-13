package routes

import (
	"github.com/UnfamousThomas/thesis-example-service/internals/app"
	"github.com/UnfamousThomas/thesis-example-service/internals/handlers"
	"log"
	"net/http"
)

// SetupRoutes sets up the nessecary routes, their handlers and starts serving http.
func SetupRoutes(a *app.App) {
	a.Mux.HandleFunc("POST /metrics", handlers.AddMetrics(a))
	a.Mux.HandleFunc("POST /scale", handlers.ScaleGame(a))
	err := http.ListenAndServe(":8080", a.Mux)
	if err != nil {
		log.Fatalf("Error starting server: %v", err)
	}
}
