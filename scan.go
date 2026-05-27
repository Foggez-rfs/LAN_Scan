package scan

import (
	"fmt"
	"os/exec"
	"strconv"
	"strings"
	"time"
)

type PortInfo struct {
	Port    int
	Proto   string
	Service string
	Version string
}

type ScanResult struct {
	Target    string
	Mode      string
	OpenPorts []PortInfo
	CVEs      []string
	RawOutput string
	Duration  string
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

func ScanTarget(target string, modeNum string) (*ScanResult, error) {
	args, ok := ScanModes[modeNum]
	if !ok {
		return nil, fmt.Errorf("неверный режим: %s", modeNum)
	}

	start := time.Now()
	fullArgs := strings.Fields(args + " " + target)
	cmd := exec.Command("nmap", fullArgs...)
	output, err := cmd.Output()
	duration := time.Since(start).Round(time.Second).String()

	result := &ScanResult{
		Target:    target,
		Mode:      modeNum,
		RawOutput: string(output),
		Duration:  duration,
	}

	if err != nil {
		return result, err
	}

	parseOutput(result)
	return result, nil
}

func ScanNetwork(subnet string) (*ScanResult, error) {
	start := time.Now()
	cmd := exec.Command("nmap", "-sn", subnet, "-oG", "-")
	output, err := cmd.Output()
	duration := time.Since(start).Round(time.Second).String()

	result := &ScanResult{
		Target:    subnet,
		Mode:      "network",
		RawOutput: string(output),
		Duration:  duration,
	}

	if err != nil {
		return result, err
	}

	// Парсим живые хосты
	lines := strings.Split(string(output), "\n")
	for _, line := range lines {
		if strings.Contains(line, "Status: Up") {
			fields := strings.Fields(line)
			if len(fields) >= 2 {
				result.OpenPorts = append(result.OpenPorts, PortInfo{
					Port:    0,
					Proto:   "host",
					Service: fields[1],
				})
			}
		}
	}

	return result, nil
}

func parseOutput(r *ScanResult) {
	lines := strings.Split(r.RawOutput, "\n")
	for _, line := range lines {
		line = strings.TrimSpace(line)

		if strings.Contains(line, "/tcp") && strings.Contains(line, "open") {
			fields := strings.Fields(line)
			if len(fields) >= 3 {
				portProto := strings.Split(fields[0], "/")
				port, _ := strconv.Atoi(portProto[0])
				pi := PortInfo{
					Port:    port,
					Proto:   "tcp",
					Service: fields[2],
				}
				if len(fields) > 3 {
					pi.Version = strings.Join(fields[3:], " ")
				}
				r.OpenPorts = append(r.OpenPorts, pi)
			}
		}

		if strings.Contains(line, "CVE-") {
			start := strings.Index(line, "CVE-")
			if start >= 0 {
				end := start + 13
				if end > len(line) {
					end = len(line)
				}
				cve := strings.TrimRight(strings.Fields(line[start:end])[0], ".:,;\"')]}")
				if strings.HasPrefix(cve, "CVE-") {
					r.CVEs = append(r.CVEs, cve)
				}
			}
		}
	}
}

func GetLocalSubnet() string {
	cmd := exec.Command("ip", "-o", "-4", "addr", "show")
	out, _ := cmd.Output()
	for _, line := range strings.Split(string(out), "\n") {
		if strings.Contains(line, "inet ") && !strings.Contains(line, "127.0.0.1") {
			for _, f := range strings.Fields(line) {
				if strings.Contains(f, "/") && !strings.Contains(f, "/32") {
					return f
				}
			}
		}
	}
	return "192.168.1.0/24"
}
