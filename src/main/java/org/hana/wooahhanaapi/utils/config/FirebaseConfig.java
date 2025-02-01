package org.hana.wooahhanaapi.utils.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @Value("${FIREBASE_CREDENTIALS}")
    private String firebaseCredentials;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (!firebaseApps.isEmpty()) {
            return firebaseApps.get(0);
        }

        if (firebaseCredentials == null || firebaseCredentials.isEmpty()) {
            throw new IllegalStateException("Firebase credentials not found in environment variables.");
        }

        String fixedCredentials = firebaseCredentials.replace("\\n", "\n");

        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(fixedCredentials.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
