package handlers

import (
	"encoding/json"
	"github.com/UnfamousThomas/thesis-example-service/internals/app"
	"log"
	"net/http"
)

type ScaleRequest struct {
	GameName        string `json:"game_name"`
	CurrentReplicas int    `json:"current_replicas"`
}

type ScaleResponse struct {
	Scale           bool `json:"scale"`
	DesiredReplicas int  `json:"desired_replicas"`
}

// ScaleGame is used by the GameAutoscaler resource to ask if the gametype should be scaled
func ScaleGame(a *app.App) func(http.ResponseWriter, *http.Request) {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		var request ScaleRequest
		err := json.NewDecoder(r.Body).Decode(&request)
		if err != nil {
			log.Printf("Error decoding metric: %v", err)
			w.WriteHeader(http.StatusBadRequest)
			return
		}

		replicas := a.Store.CalculateServerAmount(request.GameName)
		var response ScaleResponse
		if replicas == request.CurrentReplicas {
			response = ScaleResponse{
				Scale: false,
			}
		} else {
			response = ScaleResponse{
				Scale:           true,
				DesiredReplicas: replicas,
			}
		}
		w.Header().Set("Content-Type", "application/json")
		err = json.NewEncoder(w).Encode(response)
		if err != nil {
			log.Printf("Error encoding response: %v", err)
			w.WriteHeader(http.StatusInternalServerError)
			return
		}
	})
}
