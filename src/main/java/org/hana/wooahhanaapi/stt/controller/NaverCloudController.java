package org.hana.wooahhanaapi.stt.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.stt.service.NaverCloud;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class NaverCloudController {

    private final NaverCloud naverCloud;

    @PostMapping("/fileUpload")
    public String fileUpload(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest req) {
        System.out.println("NaverCloudController STT: 파일 업로드 시작");

        // 업로드 경로 설정
        String uploadPath = req.getServletContext().getRealPath("/upload");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean isCreated = uploadDir.mkdirs();
            if (!isCreated) {
                System.out.println("업로드 디렉토리 생성 실패: " + uploadPath);
                return "fail: 업로드 디렉토리 생성 실패";
            }
        }

        // 파일 저장 경로 설정
        String fileName = uploadFile.getOriginalFilename();
        String filePath = uploadPath + File.separator + fileName;

        // 파일 저장
        try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filePath)))) {
            os.write(uploadFile.getBytes());
        } catch (IOException e) {
            System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "fail: 파일 저장 실패";
        }

        // NaverCloud STT 호출
        String response;
        try {
            response = naverCloud.stt(filePath);
            System.out.println("NaverCloud STT 응답: " + response);
        } catch (Exception e) {
            System.err.println("NaverCloud STT 호출 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "fail: STT 호출 실패";
        }
        return response != null && !response.isEmpty() ? response : "fail: STT 응답이 비어있음";
    }
}
