package com.sma.quartz.security.jtw.client;

import com.google.gson.Gson;
import com.sma.common.tools.exceptions.InternalBusinessException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
@Slf4j
public abstract class AbstractApiImplementation {

    protected OkHttpClient okHttpClient;
    protected final Gson gson;

    protected AbstractApiImplementation() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    protected String getResponseAsString(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String result = response.body().string();
                log.info(result);
                return result;
            } else {
                throw new InternalBusinessException(failureMessage(response));
            }
        } catch (Exception e) {
            if (e instanceof InternalBusinessException) {
                throw (InternalBusinessException) e;
            } else {
                throw new InternalBusinessException("API execution error", e);
            }
        }
    }

    /*
     * close InputStream when reading completes
     */
    protected InputStream getResponseAsInputStream(Request request) {
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().byteStream();
            } else {
                throw new InternalBusinessException(failureMessage(response));
            }
        } catch (Exception e) {
            if (e instanceof InternalBusinessException) {
                throw (InternalBusinessException) e;
            } else {
                throw new InternalBusinessException("API execution error", e);
            }
        }
    }

//    protected DownloadedFile getResponseAsDownloadedFile(Request request) {
//        try {
//            Response response = okHttpClient.newCall(request).execute();
//            if(response.isSuccessful() && response.body() != null) {
//                return new DownloadedFile(
//                        response.header("Content-Type"),
//                        Util.getFileNameFromContentDeposition(response.header("Content-Disposition")),
//                        response.body().byteStream());
//            }
//            else {
//                throw new InternalBusinessException(failureMessage(response));
//            }
//        }
//        catch(Exception e) {
//            if(e instanceof InternalBusinessException) {
//                throw (InternalBusinessException)e;
//            }
//            else {
//                throw new InternalBusinessException("API execution error", e);
//            }
//        }
//    }

    private String failureMessage(Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("status code: ").append(response.code());
        if (response.body() != null) {
            try {
                sb.append(", response body: ").append(response.body().string());
            } catch (IOException e) {
                sb.append(", response body: not readable");
            }
        }
        return sb.toString();
    }

//    /*
//     * for unit test only
//     */
//    void setOkHttpClient(OkHttpClient okHttpClient) {
//        this.okHttpClient = okHttpClient;
//    }
}
