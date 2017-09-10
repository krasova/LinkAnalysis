package com.zest.link.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by KrasovaO on 8/28/2017.
 */
@Service
public class GoogleSearchService {

    @Value("${endpoint.googleSearch}")
    private String googleAllSearchEndPoint;
    @Value("${endpoint.googleSecSearch}")
    private String googleSecSearchEndPoint;
    @Value("${endpoint.googleFacebookSearch}")
    private String googleFacebookSearchEndPoint;
    @Value("${endpoint.googleTwitterSearch}")
    private String googleTwitterSearchEndPoint;
    @Value("${endpoint.googleLinkedinSearch}")
    private String googleLinkedinSearchEndPoint;
    @Value("${endpoint.googleInstagramSearch}")
    private String googleInstagramSearchEndPoint;

    @Autowired
    private RestTemplate restTemplate;

    public String getGoogleSearchResult(String name, String url) {
        String googleSearchEndPoint;
        switch (url) {
            case "sec":
                googleSearchEndPoint = googleSecSearchEndPoint;
                break;
            case "facebook":
                googleSearchEndPoint = googleFacebookSearchEndPoint;
                break;
            case "twitter":
                googleSearchEndPoint = googleTwitterSearchEndPoint;
                break;
            case "linkedin":
                googleSearchEndPoint = googleLinkedinSearchEndPoint;
                break;
            case "instagram":
                googleSearchEndPoint = googleInstagramSearchEndPoint;
                break;
            default:
                googleSearchEndPoint = googleAllSearchEndPoint;
                break;
        }
        return restTemplate.exchange(googleSearchEndPoint + "\"" + name + "\"",
                HttpMethod.GET,
                null,
                String.class)
                .getBody();
    }

    public String fetchHtmlByURL(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder content = new StringBuilder();

        // open the stream and put it into BufferedReader
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            content.append(line + "\n");
        }

        bufferedReader.close();

        return content.toString();
    }
}

