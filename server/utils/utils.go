package utils

import (
    "net"
    "os/exec"
    "strings"
)

func GetLocalIP() string {
    conn, _ := net.Dial("udp", "8.8.8.8:80")
    defer conn.Close()
    return strings.Split(conn.LocalAddr().String(), ":")[0]
}

func GetLocalSubnet() string {
    cmd := exec.Command("ip", "-o", "-4", "addr", "show")
    output, _ := cmd.Output()
    for _, line := range strings.Split(string(output), "\n") {
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

func IsValidIP(ip string) bool {
    return net.ParseIP(ip) != nil
}
