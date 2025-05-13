package app

import (
	"github.com/UnfamousThomas/thesis-example-service/internals/metrics"
	"net/http"
)

// App struct is where most of the state of the service is stored, along with the used http Mux.
type App struct {
	Mux   *http.ServeMux
	Store metrics.Store
}
