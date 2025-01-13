package org.hana.wooahhanaapi.domain.account.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;
import org.hana.wooahhanaapi.domain.account.exception.DuplicateAccountException;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class AccountCreateAdapter implements AccountCreatePort {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public AccountCreateRespDto createNewAccount(AccountCreateReqDto accountCreateReqDto) {

        try {

            String jsonBody = objectMapper.writeValueAsString(accountCreateReqDto);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/account"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json") // JSON 형식으로 전송
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());

            if(response.statusCode() == 400 || response.body().contains("이미 존재하는 계좌번호입니다.")) {
                throw new DuplicateAccountException("이미 존재하는 계좌번호입니다.");
            }
            else {
                return objectMapper.readValue(response.body(), AccountCreateRespDto.class);
            }

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
