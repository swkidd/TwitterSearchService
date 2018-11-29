package com.twittersearch.searchservice.controller;

import com.twittersearch.searchservice.domain.request.Query;
import com.twittersearch.searchservice.domain.response.QueryResult;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@RestController()
@RequestMapping("/api")
public class SearchController {

    private static final String bearerToken;
    static {
        String key, secret, encodedKey, token, url = "https://api.twitter.com/oauth2/token";
        String cmd = "C:/Users/skidd/AppData/Local/UserSecrets/twitterapikeys.json.txt";
        try {

            JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get(cmd)), "UTF-8"));
            key = json.get("key").toString();
            secret = json.get("secret").toString();
            encodedKey = Base64.getEncoder().encodeToString(new String(key + ":" + secret).getBytes());

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + encodedKey);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write("grant_type=client_credentials".getBytes());
            os.flush();
            os.close();

            String jsonResponse = "";
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String text;
            while ((text = br.readLine()) != null) {
                jsonResponse += text;
            }

            JSONObject responseObject = new JSONObject(jsonResponse);
            if (!responseObject.get("token_type").toString().equals("bearer")) {
                throw new RuntimeException("request_failed");
            }
            token = responseObject.get("access_token").toString();
        } catch (IOException e) {
            throw new RuntimeException(e.getStackTrace().toString());
        }
        bearerToken = token;
    }

    @PostMapping("/search")
    @ResponseBody QueryResult postSearch(@RequestBody Query query) {
        String url = "https://api.twitter.com/1.1/search/tweets.json?" + query;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + bearerToken);
        ResponseEntity<QueryResult> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), QueryResult.class);
        return response.getBody();
    }

    @GetMapping("/test")
    String testGet() {
        return "Hello World!";
    }
}

