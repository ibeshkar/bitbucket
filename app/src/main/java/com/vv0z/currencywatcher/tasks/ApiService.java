package com.vv0z.currencywatcher.tasks;

import com.vv0z.currencywatcher.models.ApiResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiService {


    // Get currency rates
    @GET
    Observable<ApiResponse> getCurrencyRates(@Url String url);

}
