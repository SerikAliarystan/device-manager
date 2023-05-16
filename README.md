## DEVICE-MANAGER SERVICE
This repository contains a Spring service with H2 database integration. The service provides a RESTful API for managing data using H2 as the in-memory database.

### Prerequisites
Java Development Kit (JDK) 11 or higher
Apache Maven

### Installation and Setup

#### Clone the repository:
```console
git clone https://github.com/SerikAliarystan/device-manager
```

#### Navigate to the project directory:
```console
cd device-manager
```

#### Build the project using Maven:
```console
mvn clean install
```

#### Start the application:
```console
mvn spring-boot:run
```
This will start the Spring service with H2 database.

### Usage
API Endpoints
The following API endpoints are available:
1. POST api/v1/device: take a device.
2. DELETE api/v1/device/{id}: release device.

### H2 Console
The H2 console is available for easy database management. You can access it at http://localhost:8080/h2-console. The default JDBC URL is jdbc:h2:mem:device-management, and the username and password are both sa.
