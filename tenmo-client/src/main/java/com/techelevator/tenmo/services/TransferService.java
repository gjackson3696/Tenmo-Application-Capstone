package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.apiguardian.api.API;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private String AUTH_TOKEN = "";

    public void setAuthToken(String authToken) {
        this.AUTH_TOKEN = authToken;
    }

    public Map<Integer, Transfer> getAllTransfers() {
        Map<Integer, Transfer> transfers = null;
        ParameterizedTypeReference<Map<Integer, Transfer>> responseType = new ParameterizedTypeReference<Map<Integer, Transfer>>() {};
        try{
            ResponseEntity<Map<Integer, Transfer>> response = restTemplate.exchange(API_BASE_URL + "transfers",
                    HttpMethod.GET, makeAuthEntity(), responseType);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            //BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public boolean sendMoney(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try{
            restTemplate.put(API_BASE_URL + "transaction", entity);
            return true;
        } catch (RestClientResponseException | ResourceAccessException e) {
           //BasicLogger.log(e.getMessage());
        }
        return false;
    }

    public Transfer addTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            ResponseEntity<Transfer> returnedTransfer = restTemplate.exchange(API_BASE_URL + "" +
                    "transfers", HttpMethod.POST, entity, Transfer.class);
            transfer = returnedTransfer.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            //BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public void updateTransferStatus(Transfer transfer) {
        HttpEntity<Transfer> entity = makeTransferEntity(transfer);
        try {
            restTemplate.put(API_BASE_URL + "transfers/update", entity);
        } catch (RestClientResponseException | ResourceAccessException e) {
            //BasicLogger.log(e.getMessage());
        }
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer auction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(AUTH_TOKEN);
        return new HttpEntity<>(auction, headers);
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(AUTH_TOKEN);
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
