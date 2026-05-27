package main

import (
    "log"
    "net/http"
    "server/api"
)

func main() {
    r := api.SetupRoutes()
    log.Println("Server started on :8080")
    log.Fatal(http.ListenAndServe(":8080", r))
}
