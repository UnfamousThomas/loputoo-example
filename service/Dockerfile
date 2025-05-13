FROM golang:1.22.6 as builder

WORKDIR /workspace

COPY go.mod ./

RUN go mod download
COPY . .

RUN CGO_ENABLED=0 GOARCH=amd64 go build -a -o service cmd/main.go

FROM gcr.io/distroless/static:nonroot
WORKDIR /
COPY --from=builder /workspace/service .
USER 65532:65532

ENTRYPOINT ["/service"]