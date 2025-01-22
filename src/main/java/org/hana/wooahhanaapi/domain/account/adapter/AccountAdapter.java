package org.hana.wooahhanaapi.domain.account.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.domain.account.adapter.dto.*;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.account.exception.DuplicateAccountException;
import org.hana.wooahhanaapi.domain.account.exception.TransferNotValidException;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class AccountAdapter implements AccountCreatePort, AccountTransferPort, AccountTransferRecordPort, BankCreatePort, GetAccountInfoPort {

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
            return objectMapper.readValue(response.body(), AccountCreateRespDto.class);
        }
        catch (Exception e){
            throw new DuplicateAccountException("이미 존재하는 계좌번호입니다.");
        }
    }

    @Override
    public AccountTransferRespDto createAccountTransfer(SimplifiedTransferReqDto reqDto) {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");

            // 날짜를 원하는 형식의 String으로 변환
            String formattedDate = currentDate.format(formatter1);
            String formattedTime = currentDate.format(formatter2);
            AccountTransferReqDto accountTransferReqDto = AccountTransferReqDto.builder()
                    .accountNumber(reqDto.getAccountNumber())
                    .bankTranId(reqDto.getBankTranId())//하나은행:001, 우리은행:002
                    .printContent(reqDto.getPrintContent())
                    .tranDate(formattedDate)
                    .tranTime(formattedTime)
                    .inoutType(reqDto.getInoutType())
                    .tranType("결재")
                    .tranAmt(reqDto.getTranAmt())
                    .branchName("우아하나")
                    .build();

            String jsonBody = objectMapper.writeValueAsString(accountTransferReqDto);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/transfer"))
                    .version(HttpClient.Version.HTTP_1_1)
                    .header("Content-Type", "application/json") // JSON 형식으로 전송
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            return objectMapper.readValue(response.body(), AccountTransferRespDto.class);
        }
        catch (Exception e){
            throw new TransferNotValidException("거래에 실패했습니다.");
        }
    }

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
            return objectMapper.readValue(response.body(), AccountTransferRecordRespDto.class);
        }
        catch (Exception e){
            throw new AccountNotFoundException("존재하지 않는 계좌입니다.");
        }
    }

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
            throw new AccountNotFoundException("계좌 정보를 찾을 수 없습니다.");
        }
    }
}
