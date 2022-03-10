package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private String AUTH_TOKEN = "";

    public void setAuthToken(String authToken) {
        this.AUTH_TOKEN = authToken;
    }

    public List<User> getUserList() {
        List<User> userList = null;
        ParameterizedTypeReference<List<User>> responseType = new ParameterizedTypeReference<List<User>>() {};
        try {
            ResponseEntity<List<User>> response = restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET,makeAuthEntity(),responseType);
            userList = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            //BasicLogger.log(e.getMessage());
        }
        return userList;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
