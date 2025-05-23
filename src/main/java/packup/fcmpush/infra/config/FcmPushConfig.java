package packup.fcmpush.infra.config;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import packup.fcmpush.exception.FcmPushException;

import java.io.IOException;

import static packup.fcmpush.exception.FcmPushExceptionType.FAIL_TO_LOAD_CONFIG;

@Configuration
public class FcmPushConfig {

    @Value("${files.firebase_admin_sdk_path}")
    private Resource resource;

    @Value("${firebase.app-name}")
    private String firebaseAppName;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        if(!resource.exists()) {
            throw new FcmPushException(FAIL_TO_LOAD_CONFIG);
        }

        FirebaseApp firebaseApp;
        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals(firebaseAppName))) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options, firebaseAppName);
        } else {
            firebaseApp = FirebaseApp.getInstance(firebaseAppName);
        }

        return firebaseApp;
    }


    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
