package org.hana.wooahhanaapi.utils.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hana.wooahhanaapi.utils.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.utils.adapter.dto.AccountCreateRespDto;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class MockAccountAdapter implements AccountPort{

    @Override
    public AccountCreateRespDto createNewAccount(AccountCreateReqDto accountCreateReqDto) {

        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonBody = objectMapper.writeValueAsString(accountCreateReqDto);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/account"))
                    .header("Content-Type", "application/json") // JSON 형식으로 전송
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return objectMapper.readValue(response.body(), AccountCreateRespDto.class);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
