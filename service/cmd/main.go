package main

import (
	"github.com/UnfamousThomas/thesis-example-service/internals/app"
	"github.com/UnfamousThomas/thesis-example-service/internals/metrics"
	"github.com/UnfamousThomas/thesis-example-service/internals/routes"
	"net/http"
)

func main() {

	store := metrics.New()
	a := app.App{
		Mux:   http.NewServeMux(),
		Store: store,
	}

	routes.SetupRoutes(&a)
}
