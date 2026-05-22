# Variables
GRADLEW = ./gradlew

.PHONY: all
all: build

# Install execution permissions
.PHONY: install
install:
	chmod +x gradlew

# Build all microservices
.PHONY: build
build:
	chmod +x gradlew
	$(GRADLEW) clean build -x test

# Run unit tests
.PHONY: test
test:
	chmod +x gradlew
	$(GRADLEW) test

# Run the local microservice environment in Docker
.PHONY: dev
dev:
	docker-compose up --build

# Shutdown the local containers
.PHONY: down
down:
	docker-compose down

# Clean build folders
.PHONY: clean
clean:
	chmod +x gradlew
	$(GRADLEW) clean
