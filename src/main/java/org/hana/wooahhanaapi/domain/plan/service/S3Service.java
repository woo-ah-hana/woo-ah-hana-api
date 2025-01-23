package org.hana.wooahhanaapi.domain.plan.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Log4j2
@RequiredArgsConstructor
@Service
public class S3Service {
    @Value("${S3_BUCKET_NAME}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile, String s3FileName) throws IOException {

        String encodedFileName = URLEncoder.encode(s3FileName, StandardCharsets.UTF_8);
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());
        // 파일의 MIME 타입 설정
        objMeta.setContentType(multipartFile.getContentType());
        try{
            amazonS3.putObject(bucket, encodedFileName, multipartFile.getInputStream(), objMeta);
        }catch (AmazonServiceException e) {
            log.error("S3 파일 업로드 중 오류 발생: {}", e.getMessage(), e);
        }

        return amazonS3.getUrl(bucket, encodedFileName).toString();
          //return "url";
    }

    public void delete(String fileUrl) {
        try {
            // ".com/" 이후의 파일명을 추출
            String keyName = fileUrl.substring(fileUrl.indexOf(".com/") + 5);
            amazonS3.deleteObject(bucket, keyName);
        } catch (AmazonServiceException e) {
            log.error("S3 파일 삭제 중 오류 발생: {}", e.toString());
        }
    }
}