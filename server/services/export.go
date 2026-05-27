package services

import (
    "fmt"
    "strings"
    "time"
)

func GenerateReport(target, mode, output string) string {
    var sb strings.Builder
    sb.WriteString("=" * 50 + "\n")
    sb.WriteString("SCAN REPORT\n")
    sb.WriteString("=" * 50 + "\n\n")
    sb.WriteString(fmt.Sprintf("Date: %s\n", time.Now().Format("2006-01-02 15:04:05")))
    sb.WriteString(fmt.Sprintf("Target: %s\n", target))
    sb.WriteString(fmt.Sprintf("Mode: %s\n\n", mode))
    sb.WriteString(output)
    return sb.String()
}
