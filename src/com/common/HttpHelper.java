package com.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpHelper {
    public List<String> sendGet(String requestUrl) throws Exception {
        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        List<String> response = new ArrayList<>();

        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }

        in.close();

        return response;
    }

    public List<String> extractUrl(List<String> response) {
        List<String> extractUrl = new ArrayList<>();
        Pattern urlPattern = Pattern.compile("(http|https)://[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-.,@?^=%&amp;:/~+#]*[\\w\\-@?^=%&amp;/~+#])?");

        response.stream()
                .map(urlPattern::matcher)
                .map(this::getMatcherString)
                .distinct()
                .filter(s -> s.length() != 0)
                .sorted(Comparator.comparing(String::length).reversed())
                .limit(10)
                .forEach(s -> {
                    System.out.println(s.length() + " " + s);
                    extractUrl.add(s);
                });

        System.out.println();

        return extractUrl;
    }

    private String getMatcherString(Matcher m) {
        return m.find() ? m.group(0) : "";
    }

    public void calculateResponseTime(List<String> extractUrl) throws Exception {
        Map<String, Double> map = new HashMap<>();

        for (String url : extractUrl) {
            long startTime = System.currentTimeMillis();
            sendGet(url);
            long endTime = System.currentTimeMillis();

            map.put(url, (endTime - startTime) / 1000D);
        }

        List<String> keySetList = new ArrayList<>(map.keySet());

        keySetList.stream()
                .sorted((o1, o2) -> (map.get(o2).compareTo(map.get(o1))))
                .forEach(url -> System.out.println(map.get(url) + " " + url));
    }
}