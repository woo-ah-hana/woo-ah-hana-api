package org.hana.wooahhanaapi.domain.activeplan.adaptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.dto.SearchResponseDto;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class NaverSearchAdaptor implements NaverSearchPort{

    @Override
    public SearchResponseDto getSearchResult(String query) {

        String clientId = "UAGg85a6riglTDRBSJqQ";
        String clientSecret = "B9Yr2U8onG";

        String text = query;
        try{
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e){
            throw new RuntimeException("검색어 인코딩 실패", e);
        }

        // URL만 수정하여 API를 사용
        String apiURL = "https://openapi.naver.com/v1/search/local.json?query=" + text;    // JSON 결과
        //String apiURL = "https://openapi.naver.com/v1/search/local.xml?query="+ text; // XML 결과

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        String responseBody = get(apiURL, requestHeaders);
        // Jackson ObjectMapper 생성
        ObjectMapper objectMapper = new ObjectMapper();

        SearchResponseDto responseDto = null;
        try {
            // responseBody를 SearchResponseDto로 변환
            responseDto = objectMapper.readValue(responseBody, SearchResponseDto.class);

            // 결과 출력
            System.out.println("Last Build Date: " + responseDto.getLastBuildDate());
            System.out.println("Total: " + responseDto.getTotal());
            if (!responseDto.getItems().isEmpty()) {
                System.out.println("First Item Title: " + responseDto.getItems().get(0).getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(responseBody);
        return responseDto;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
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
