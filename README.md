# PayPal Integration with Spring Boot

This project demonstrates integration with PayPal's REST API (sandbox environment) using Spring Boot. The application includes functionalities for generating an access token, creating payment requests, and handling payment redirection. This project is designed to showcase PayPal integration skills for any portfolio or demo purposes.

---

## **Features**
- OAuth2 authentication with PayPal's REST API.
- Payment request creation with customizable details.
- User redirection to PayPal for payment approval.
- Structured code with modular design for clarity and maintainability.
- Ready for deployment with Docker.

---

## **Technologies Used**
- **Java 11+**: Programming language.
- **Spring Boot**: Framework for backend development.
- **RestTemplate**: HTTP client for REST API calls.
- **Jackson ObjectMapper**: For JSON processing.
- **Docker**: For containerized deployment.
- **Maven**: Dependency and build tool.

---

## **Getting Started**

### **Prerequisites**
1. **Java 11 or higher** installed.
2. **Maven** for dependency management.
3. **Docker** (optional for deployment).
4. **PayPal Developer Account**:
    - Sign up at [PayPal Developer Portal](https://developer.paypal.com/).
    - Create an app to obtain your `Client ID` and `Secret`.

---

### **Setting Up the Project**

#### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/paypal-integration.git
cd paypal-integration

---

1. **New Endpoints**:
   - `/api/paypal/access-token`: Fetches the OAuth2 token directly.
   - `/api/paypal/create-payment`: Creates a payment dynamically based on request payload.

2. **Environment Variable Support**:
   - Easy configuration for deployment environments like Heroku or Docker.

3. **Postman Collection**:
   - Added a Postman collection for testing the API easily.

4. **Dockerfile**:
   - The project can now be run as a container.

---

### **Dockerfile**
Add the following `Dockerfile` to your project root:

```dockerfile
# Use OpenJDK image
FROM openjdk:11-jdk-slim

# Set working directory
WORKDIR /app

# Copy project JAR file
COPY target/paypal-integration-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
