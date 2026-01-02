package com.vthreads.booktrip.api.client;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public abstract class BaseServiceClient {

    protected final RestClient client;

    protected BaseServiceClient(RestClient client ) {
        this.client = client;
    }

    protected <R> List<R>  fetchResponseList(String uri, ParameterizedTypeReference<List<R>> responseType, Object... vars){
        return client.get()
                .uri(uri, vars)
                .retrieve()
                .body(responseType);
    }

    protected <R> R fetchResponse(String uri, ParameterizedTypeReference<R> responseType,  Object... vars){
        return client.get()
                .uri(uri, vars)
                .retrieve()
                .body(responseType);
    }
}
