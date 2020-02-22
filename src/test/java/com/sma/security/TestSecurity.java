//package com.sma.security;
//
//import com.sma.security.security.jtw.client.SecurityApi;
//import com.sma.security.security.jtw.client.SecurityApiBuilder;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @author Mak Sophea
// * @date : 12/27/2019
// **/
//@Slf4j
//public class TestSecurity {
//    public static void main(String[] args){
//
//        SecurityApi api = SecurityApiBuilder.newBuilder().withSecApiUrl("http://localhost:8081/oauth2/token")
//                .withClientId("f8e07c63-6fab-4cd0-80ee-f9b46eaabef4")
//                .withClientSecret("abc@2019")
//                .build();
//
//        //new token
//        log.info("=========={}", api.getAccessToken());
//        //old token get from cache
//        log.info("=========={}", api.getAccessToken());
//
//    }
//}
