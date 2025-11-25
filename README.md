# Trip AI Search

This project is a Spring Boot application that allows searching for trip data using AI-powered filters.

## Prerequisites

-   Java 17 or higher
-   Maven

## Configuration

### Environment Variables

You need to set the `AI_CLIENT_TOKEN` environment variable with your AI service token before running the application.

```bash
export AI_CLIENT_TOKEN=your_token_here
# or on Windows PowerShell
$env:AI_CLIENT_TOKEN="your_token_here"
```

## Database Setup

The application uses an H2 database. You need to manually create the table and import the data.

1.  Start the application.
2.  Access the H2 Console at `http://localhost:8080/h2-console`.
    -   **JDBC URL**: `jdbc:h2:file:./data/trips;NON_KEYWORDS=VALUE`
    -   **User Name**: `sa`
    -   **Password**: (leave empty)
3.  Run the following SQL script to create the table:

```sql
CREATE TABLE trip (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vendor_id VARCHAR(10),
    pickup_datetime TIMESTAMP NOT NULL,
    dropoff_datetime TIMESTAMP NOT NULL,
    passenger_count INTEGER NOT NULL,
    trip_distance DECIMAL(10, 2) NOT NULL,
    rate_code INTEGER,
    store_and_fwd_flag VARCHAR(1),
    payment_type VARCHAR(20),
    fare_amount DECIMAL(10, 2),
    extra DECIMAL(10, 2),
    mta_tax DECIMAL(10, 2),
    tip_amount DECIMAL(10, 2),
    tolls_amount DECIMAL(10, 2),
    imp_surcharge DECIMAL(10, 2),
    total_amount DECIMAL(10, 2),
    pickup_location_id INTEGER,
    dropoff_location_id INTEGER
);
```

4.  Run the following SQL command to import the data from the CSV file:

```sql
INSERT INTO trip (
    vendor_id, pickup_datetime, dropoff_datetime, passenger_count, trip_distance, rate_code, 
    store_and_fwd_flag, payment_type, fare_amount, extra, mta_tax, tip_amount, tolls_amount, 
    imp_surcharge, total_amount, pickup_location_id, dropoff_location_id
) 
SELECT * FROM CSVREAD('./src/main/resources/static/nyc_tlc_yellow_trips_2018_subset_1.csv');
```

> **Note:** Ensure the CSV file path is correct relative to where you started the application.

## Running the Application

To run the application, use the following command:

```bash
mvn spring-boot:run
```
