source_file=$(shell go list ./...)

test:
	go test ./... -count=1

coverage:
	go test $(source_file) -coverprofile=coverage.out
