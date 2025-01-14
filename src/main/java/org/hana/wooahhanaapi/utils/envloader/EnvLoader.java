package org.hana.wooahhanaapi.utils.envloader;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EnvLoader {

    @PostConstruct
    public void init() {
        Dotenv dotenv = Dotenv.load();  // .env 파일을 로드하여 환경 변수로 등록
    }
}
