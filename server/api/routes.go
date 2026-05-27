package api

import (
    "github.com/gorilla/mux"
)

func SetupRoutes() *mux.Router {
    r := mux.NewRouter()
    r.HandleFunc("/api/scan", ScanHandler).Methods("POST")
    r.HandleFunc("/api/scan/network", NetworkScanHandler).Methods("POST")
    r.HandleFunc("/api/cve/{id}", CVEHandler).Methods("GET")
    r.HandleFunc("/api/export", ExportHandler).Methods("POST")
    r.HandleFunc("/ws", WSHandler)
    return r
}
