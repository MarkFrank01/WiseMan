package com.zxcx.shitang.retrofit;

import com.zxcx.shitang.ui.classify.ClassifyBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface APIService {

    String API_SERVER_URL = "";

    @GET("")
    Observable<ClassifyBean> getAllCardBag();
}
