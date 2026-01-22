# Spring-Batch-Demo

A Spring Batch demonstration project that processes customer data from CSV files into a PostgreSQL database with
configurable filtering capabilities.

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

## Setup

1. Clone the repository
2. Start the PostgreSQL database:
   ```bash
   cd docker
   docker-compose up -d
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```

## Usage

The application processes customer data from a CSV file with the following features:

- Reads customer records from `customers.csv`
- Filters customers based on country (configurable via job parameters)
- Validates job parameters (blocks processing for specific countries)
- Writes processed records to PostgreSQL database

### Job Parameters

- `ignoreCountry`: Specifies which country's customers to filter out
    - Example: `ignoreCountry=USA`
    - Note: Setting `ignoreCountry=Italy` will fail validation

## Configuration

### Database Configuration

The application uses PostgreSQL.
You can modify these settings in the `application.yaml` file.
