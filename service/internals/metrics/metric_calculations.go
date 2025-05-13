package metrics

import "math"

// MULTIPLIER_OF_PLAYER_SLOTS defines what percentage of player slots should be free.  For 1.2 it means 20% should be free.
const MULTIPLIER_OF_PLAYER_SLOTS = 1.2

// EXTRA_SLOTS defines how many extra slots should exist
const EXTRA_SLOTS = 10

// SLOTS_PER_SERVER defines how many "slots" exist for each server
const SLOTS_PER_SERVER = 2

// CalculateServerAmount is used to calculate how many replicas should exist for a gametype
func (s *Store) CalculateServerAmount(gameName string) int {
	s.CleanMetrics(gameName)
	metrics := s.GetMetricsForGame(gameName)
	totalPlayers := 0
	for _, met := range metrics {
		totalPlayers += met.PlayerCount
	}
	requiredSlots := int(math.Ceil(float64(totalPlayers)*MULTIPLIER_OF_PLAYER_SLOTS)) + EXTRA_SLOTS
	requiredServers := int(math.Ceil(float64(requiredSlots) / float64(SLOTS_PER_SERVER)))

	return requiredServers
}
