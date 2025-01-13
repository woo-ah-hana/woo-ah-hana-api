package org.hana.wooahhanaapi.domain.account.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.domain.account.adapter.dto.GetAccountInfoReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.GetAccountInfoRespDto;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class GetAccountInfoAdapter implements GetAccountInfoPort {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GetAccountInfoRespDto getAccountInfo(GetAccountInfoReqDto getAccountInfoReqDto){

        try{
            String jsonBody = objectMapper.writeValueAsString(getAccountInfoReqDto);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/account/info"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            return objectMapper.readValue(response.body(), GetAccountInfoRespDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
