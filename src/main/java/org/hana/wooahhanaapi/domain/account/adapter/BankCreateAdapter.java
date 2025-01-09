package org.hana.wooahhanaapi.domain.account.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateRespDto;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class BankCreateAdapter implements BankCreatePort{

    @Override
    public BankCreateRespDto createNewBank(BankCreateReqDto requestDto) {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonBody = objectMapper.writeValueAsString(requestDto);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/bank"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());

            return objectMapper.readValue(response.body(), BankCreateRespDto.class);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
