package za.co.ish_project.paypal_integration.paypal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PaypalService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.api-url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Method to fetch the access token
    public String getAccessToken() {
        String url = apiUrl + "/v1/oauth2/token";

        // Encode credentials
        String credentials = clientId + ":" + clientSecret;
        String base64Credentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + base64Credentials);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Set body
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Make API call
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        // Parse the access token from JSON response
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token: " + e.getMessage(), e);
        }
    }

    // Method to create a payment
    public String createPayment() {
        String url = apiUrl + "/v1/payments/payment";

        // Build the payment request payload
        Map<String, Object> paymentRequest = buildPaymentRequestPayload();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(paymentRequest, headers);

        // Make API call
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        return response.getBody();
    }

    // Helper method to build the payment request payload
    private Map<String, Object> buildPaymentRequestPayload() {
        Map<String, Object> paymentPayload = new HashMap<>();

        // Payment intent: 'sale' for an immediate payment
        paymentPayload.put("intent", "sale");

        // Payer information
        Map<String, String> payer = new HashMap<>();
        payer.put("payment_method", "paypal");
        paymentPayload.put("payer", payer);

        // Transaction details
        Map<String, Object> transaction = new HashMap<>();
        Map<String, String> amount = new HashMap<>();
        amount.put("total", "10.00"); // Total amount in the specified currency
        amount.put("currency", "USD");
        transaction.put("amount", amount);

        transaction.put("description", "Payment for a product/service.");

        paymentPayload.put("transactions", new Map[]{transaction});

        // Redirect URLs
        Map<String, String> redirectUrls = new HashMap<>();
        redirectUrls.put("return_url", "http://localhost:8080/success");
        redirectUrls.put("cancel_url", "http://localhost:8080/cancel");
        paymentPayload.put("redirect_urls", redirectUrls);

        return paymentPayload;
    }
}






