package com;

import com.common.HttpHelper;

import java.util.List;

public class Main {
    private static final String daumUrl = "https://www.daum.net";

    public static void main(String[] args) throws Exception {
        HttpHelper httpHelper = new HttpHelper();

        List<String> response = httpHelper.sendGet(daumUrl); // get 요청
        List<String> extractUrl = httpHelper.extractUrl(response); // url 추출
        httpHelper.calculateResponseTime(extractUrl); // 응답시간 게산
    }
}