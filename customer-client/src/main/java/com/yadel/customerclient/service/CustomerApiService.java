package com.yadel.customerclient.service;

import com.yadel.customerclient.model.Customer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Handles all HTTP communication with the backend API.
 * @author y.adel
 */
public class CustomerApiService {
    private static final String BASE_URL = "http://localhost:8080/api/customers";
    private static final String API_KEY = "my-secret-token";
    private final HttpClient httpClient;
    private final Gson gson;

    public CustomerApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    }

    private HttpRequest.Builder requestBuilder(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(30));
    }

    public List<Customer> getAllCustomers() throws Exception {
        HttpRequest request = requestBuilder(BASE_URL).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), new TypeToken<List<Customer>>(){}.getType());
        } else {
            throw new RuntimeException("Failed to fetch customers: HTTP " + response.statusCode());
        }
    }

    public Customer createCustomer(Customer customer) throws Exception {
        String json = gson.toJson(customer);
        HttpRequest request = requestBuilder(BASE_URL)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return gson.fromJson(response.body(), Customer.class);
        } else {
            throw new RuntimeException("Failed to create customer: " + response.body());
        }
    }

    public void updateCustomer(Long id, Customer customer) throws Exception {
        String json = gson.toJson(customer);
        HttpRequest request = requestBuilder(BASE_URL + "/" + id)
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new RuntimeException("Failed to update customer: HTTP " + response.statusCode());
        }
    }

    public void deleteCustomer(Long id) throws Exception {
        HttpRequest request = requestBuilder(BASE_URL + "/" + id).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 204) {
            throw new RuntimeException("Failed to delete customer: HTTP " + response.statusCode());
        }
    }
}