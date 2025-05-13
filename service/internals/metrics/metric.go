package metrics

import (
	"time"
)

// Metric is used to receive metric info from servers
type Metric struct {
	PlayerCount int    `json:"player_count"`
	Timestamp   int64  `json:"time_unix"`
	ServerName  string `json:"server"`
	GameName    string `json:"game_type"`
	FleetName   string `json:"fleet"`
}

// Store is where Metric are stored, in real life this would be some form of database
type Store map[string]map[string]Metric

// New is a utility function to make a new Store
func New() Store {
	return make(Store)
}

// AddOrUpdateMetric overwrites or creates new metric in the Store
func (s *Store) AddOrUpdateMetric(m Metric) {
	if _, ok := (*s)[m.GameName]; !ok {
		(*s)[m.GameName] = make(map[string]Metric)
	}
	(*s)[m.GameName][m.ServerName] = m
}

// GetMetricsForGame gets the metrics for a certain gametype
func (s *Store) GetMetricsForGame(gameType string) []Metric {
	if gameMetrics, ok := (*s)[gameType]; ok {
		metrics := make([]Metric, 0, len(gameMetrics))
		for _, m := range gameMetrics {
			metrics = append(metrics, m)
		}
		return metrics
	}
	return nil
}

// CleanMetrics deletes all metrics that are older than 10 minutes for a certain gameType
func (s *Store) CleanMetrics(gameType string) {
	if gameMetrics, ok := (*s)[gameType]; !ok {
		(*s)[gameType] = make(map[string]Metric)
		return
	} else {
		for _, m := range gameMetrics {
			unixTime := time.UnixMilli(m.Timestamp)
			if unixTime.Add(time.Minute * 10).Before(time.Now()) {
				delete(gameMetrics, m.ServerName)
			}
		}
	}
}
