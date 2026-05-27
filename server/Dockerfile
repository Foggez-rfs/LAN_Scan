FROM golang:1.21-alpine
RUN apk add --no-cache nmap
WORKDIR /app
COPY . .
RUN go build -o server .
EXPOSE 8080
CMD ["./server"]
