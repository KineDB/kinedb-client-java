package com.itenebris.kinedb.jdbc.executor;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.itenebris.kinedb.jdbc.connection.DriverUri;
import com.itenebris.kinedb.jdbc.result.ResultData;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Deprecated
public class HttpQueryExecutor {
    Logger log = LoggerFactory.getLogger(HttpQueryExecutor.class);

    private final OkHttpClient client;
    private final String uri;
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String api = "/v1/execute-statement";
    private final Gson gson = new Gson();

    public HttpQueryExecutor(DriverUri uri) {
        this.uri = uri.getHttpUri();
        log.debug("http uri: " + this.uri);
        client = new OkHttpClient();
    }

    public ResultData executeSql(String sql) throws Exception {
        String url = uri + api;
        log.debug("http url: " + url);
        String postBody = "{\"sql\":\"" + sql + "\"}";
        log.debug("post body: " + postBody);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(postBody, JSON))
                .build();

        Response response = client.newCall(request).execute();
        String json = response.body().string();
        log.debug("post response body :" + json);

        KineResponse kineResponse = gson.fromJson(json, KineResponse.class);
        System.out.println("kine response :" + gson.toJson(kineResponse));
        if (kineResponse.getCode() != 200) {
            throw new SQLException(kineResponse.getMessage());
        }

        KineData result = kineResponse.getData();
        result.buildIndexMapping();
        return result;
    }
}
