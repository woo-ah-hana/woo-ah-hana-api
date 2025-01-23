package org.hana.wooahhanaapi.domain.stt.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class NaverCloud {
    public static String stt(String filePath) {
        String clientId = "ninr2zu0qx";  // Application Client ID
        String clientSecret = "kmIOFaR83NOPtrO4JmdH3VzXKqPrg6P7pHsI7UXz";  // Application Client Secret
        StringBuffer response = new StringBuffer();

        try {
            File voiceFile = new File(filePath);
            if (!voiceFile.exists()) {
                throw new FileNotFoundException("음성 파일을 찾을 수 없습니다: " + filePath);
            }

            String language = "Kor";  // 언어 코드 (Kor, Jpn, Eng, Chn)
            String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            // 파일 업로드
            try (OutputStream outputStream = conn.getOutputStream();
                 FileInputStream inputStream = new FileInputStream(voiceFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            // 응답 처리
            int responseCode = conn.getResponseCode();
            System.out.println("Naver API Response Code: " + responseCode);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()
                    ))) {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
            }

            if (responseCode != 200) {
                System.err.println("NaverCloud STT 오류: " + response.toString());
                return "fail: STT 호출 실패 - " + responseCode;
            }

        } catch (IOException e) {
            System.err.println("NaverCloud STT IOException: " + e.getMessage());
            e.printStackTrace();
            return "fail: IOException 발생";
        } catch (Exception e) {
            System.err.println("NaverCloud STT Exception: " + e.getMessage());
            e.printStackTrace();
            return "fail: Exception 발생";
        }

        return response.toString();
    }
}
