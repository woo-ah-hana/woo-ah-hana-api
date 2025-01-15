package org.hana.wooahhanaapi.domain.naver.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hana.wooahhanaapi.domain.naver.adaptor.dto.SearchResponseDto;
import org.hana.wooahhanaapi.domain.naver.exception.EmptyResponseBodyException;
import org.hana.wooahhanaapi.domain.naver.exception.InvalidSearchQueryException;
import org.hana.wooahhanaapi.domain.naver.exception.NaverApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class NaverSearchAdaptor implements NaverSearchPort{

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    @Override
    public List<SearchResponseDto> getSearchResultList(List<String> queries) {
        // 중복되거나 유효하지 않은 검색어를 필터링하여 처리
        Set<String> uniqueQueries = new HashSet<>();
        List<String> validQueries = queries.stream()
                .filter(query -> query != null && !query.trim().isEmpty() && uniqueQueries.add(query))  // 유효한 검색어만 필터링
                .toList();

        if (validQueries.isEmpty()) {
            throw new InvalidSearchQueryException("유효한 검색어가 없습니다.");
        }

        List<CompletableFuture<SearchResponseDto>> futures = validQueries.stream()
                .map(query -> CompletableFuture.supplyAsync(() -> getSearchResult(query)))
                .toList();

        return futures.stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException("병렬 처리 중 오류 발생", e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public SearchResponseDto getSearchResult(String query) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + encodedQuery + "&sort=comment&display=3";

            Map<String, String> requestHeaders = Map.of(
                    "X-Naver-Client-Id", clientId,
                    "X-Naver-Client-Secret", clientSecret
            );

            String responseBody = get(apiURL, requestHeaders);

            if (responseBody == null || responseBody.isEmpty()) {
                throw new EmptyResponseBodyException(query);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, SearchResponseDto.class);

        } catch (IOException e) {
            throw new NaverApiException("검색어 처리 중 문제가 발생했습니다: " + e.getMessage());
        }
    }


    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

}
