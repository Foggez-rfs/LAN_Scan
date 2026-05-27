package models

import "time"

type ScanResult struct {
    ID        string    `json:"id"`
    Target    string    `json:"target"`
    Mode      string    `json:"mode"`
    Output    string    `json:"output"`
    CreatedAt time.Time `json:"created_at"`
}

type CVEInfo struct {
    ID          string `json:"id"`
    Description string `json:"description"`
    CVSS        float64 `json:"cvss"`
    Severity    string `json:"severity"`
}

type Device struct {
    IP   string `json:"ip"`
    MAC  string `json:"mac"`
    Name string `json:"name"`
}
