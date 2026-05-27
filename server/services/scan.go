package services

import (
    "os/exec"
    "strings"
)

func RunNmap(args string) (string, error) {
    cmd := exec.Command("nmap", strings.Fields(args)...)
    output, err := cmd.Output()
    return string(output), err
}

func DiscoverNetwork(subnet string) ([]string, error) {
    output, err := RunNmap("-sn " + subnet)
    if err != nil {
        return nil, err
    }
    var hosts []string
    for _, line := range strings.Split(output, "\n") {
        if strings.Contains(line, "Nmap scan report for") {
            hosts = append(hosts, strings.Fields(line)[4])
        }
    }
    return hosts, nil
}
