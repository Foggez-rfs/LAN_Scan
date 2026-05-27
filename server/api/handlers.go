package api

import (
    "encoding/json"
    "net/http"
    "os/exec"
    "strings"
)

type ScanRequest struct {
    Target string `json:"target"`
    Mode   string `json:"mode"`
}

type ScanResponse struct {
    Target string `json:"target"`
    Output string `json:"output"`
    Error  string `json:"error,omitempty"`
}

var ScanModes = map[string]string{
    "1":  "-Pn --top-ports 100 --min-rate 1000",
    "2":  "-sV -Pn --version-intensity 5",
    "3":  "-O -Pn --osscan-guess",
    "4":  "-sC -Pn --script=default",
    "5":  "-A -Pn -T4",
    "6":  "-p- -Pn --min-rate 5000",
    "7":  "-sU -Pn --top-ports 100",
    "8":  "-Pn --script vuln --script-args mincvss=5.0",
    "9":  "-sC -sV -A -v -Pn -T4 --script=vuln,exploit,auth,default",
    "10": "-p- -sC -sV -A -Pn -T5 --script=vuln,exploit --min-rate 10000",
}

func ScanHandler(w http.ResponseWriter, r *http.Request) {
    var req ScanRequest
    json.NewDecoder(r.Body).Decode(&req)
    
    args, ok := ScanModes[req.Mode]
    if !ok {
        args = "-sV -Pn"
    }
    
    cmd := exec.Command("nmap", strings.Fields(args+" "+req.Target)...)
    output, err := cmd.Output()
    
    resp := ScanResponse{
        Target: req.Target,
        Output: string(output),
    }
    if err != nil {
        resp.Error = err.Error()
    }
    
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(resp)
}

func NetworkScanHandler(w http.ResponseWriter, r *http.Request) {
    var req ScanRequest
    json.NewDecoder(r.Body).Decode(&req)
    
    subnet := req.Target
    if subnet == "" {
        subnet = "192.168.1.0/24"
    }
    
    cmd := exec.Command("nmap", "-sn", subnet)
    output, _ := cmd.Output()
    
    json.NewEncoder(w).Encode(ScanResponse{
        Target: subnet,
        Output: string(output),
    })
}

func CVEHandler(w http.ResponseWriter, r *http.Request) {
    vars := mux.Vars(r)
    id := vars["id"]
    
    json.NewEncoder(w).Encode(map[string]string{
        "id": id,
        "url": "https://nvd.nist.gov/vuln/detail/" + id,
    })
}

func ExportHandler(w http.ResponseWriter, r *http.Request) {
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(map[string]string{"status": "ok"})
}
