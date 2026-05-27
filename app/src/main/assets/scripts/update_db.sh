#!/bin/sh
echo "Updating CVE database..."
curl -s "https://services.nvd.nist.gov/rest/json/cves/2.0?resultsPerPage=100" -o /tmp/cve.json
echo "Done"
