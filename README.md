# restaurant-service

A Spring Boot microservice for finding nearby restaurants with Basic Authentication.

## Features

- ✅ Restaurant data model with location coordinates
- ✅ Basic Authentication security 
- ✅ getNearRestaurant API endpoint
- ✅ Distance calculation using Haversine formula
- ✅ H2 in-memory database with sample data

## API Endpoints

### Get Nearby Restaurants
`GET /api/restaurants/near`

**Parameters:**
- `latitude` (required): User's latitude coordinate
- `longitude` (required): User's longitude coordinate  
- `radius` (optional): Search radius in kilometers (default: 5.0)

**Authentication:** Basic Auth
- Username: `restaurant-user`
- Password: `restaurant-pass`

**Example:**
```bash
curl -u restaurant-user:restaurant-pass \
  "http://localhost:8080/api/restaurants/near?latitude=40.7580&longitude=-73.9855&radius=2.0"
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Pizza Palace",
    "address": "123 Main St, New York, NY",
    "latitude": 40.758,
    "longitude": -73.9855,
    "phone": "555-0101",
    "cuisineType": "Italian",
    "distance": 0.0
  }
]
```

## Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing

```bash
mvn test
```

## H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)