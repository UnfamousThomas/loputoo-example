package handlers

import (
	"encoding/json"
	"github.com/UnfamousThomas/thesis-example-service/internals/app"
	"github.com/UnfamousThomas/thesis-example-service/internals/metrics"
	"log"
	"net/http"
)

// AddMetrics is used by the server to report its state
func AddMetrics(a *app.App) func(http.ResponseWriter, *http.Request) {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {

		var request metrics.Metric
		err := json.NewDecoder(r.Body).Decode(&request)
		if err != nil {
			log.Printf("Error decoding metric: %v", err)
			w.WriteHeader(http.StatusBadRequest)
			return
		}
		a.Store.AddOrUpdateMetric(request)
		w.Header().Set("Content-Type", "application/json")
		err = json.NewEncoder(w).Encode(request)
		if err != nil {
			log.Printf("Error encoding response: %v", err)
			w.WriteHeader(http.StatusInternalServerError)
			return
		}
	})
}
