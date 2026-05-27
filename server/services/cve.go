package services

import (
    "encoding/json"
    "net/http"
)

type NVDResponse struct {
    Vulnerabilities []struct {
        CVE struct {
            ID          string `json:"id"`
            Description struct {
                Value string `json:"value"`
            } `json:"description"`
        } `json:"cve"`
    } `json:"vulnerabilities"`
}

func FetchCVE(cveID string) (*NVDResponse, error) {
    url := "https://services.nvd.nist.gov/rest/json/cves/2.0?cveId=" + cveID
    resp, err := http.Get(url)
    if err != nil {
        return nil, err
    }
    defer resp.Body.Close()
    var result NVDResponse
    json.NewDecoder(resp.Body).Decode(&result)
    return &result, nil
}
