package org.hana.wooahhanaapi.domain.account.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespDto;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AccountTransferRecordAdapter implements AccountTransferRecordPort {
    // HttpClient 객체를 클래스 수준에서 재사용
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public AccountTransferRecordRespDto getTransferRecord(AccountTransferRecordReqDto reqDto) {

        try {
            String jsonBody = objectMapper.writeValueAsString(reqDto);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/transfer/list"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json") // JSON 형식으로 전송
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
            if(response.statusCode() == 400 || response.body().contains("존재하지 않는 계좌입니다.")) {
                throw new AccountNotFoundException("존재하지 않는 계좌입니다.");
            }
            else {
                return objectMapper.readValue(response.body(), AccountTransferRecordRespDto.class);
            }

        }
        catch (Exception e){
            throw new RuntimeException("Error occurred while fetching transfer", e);
        }
    }
}
