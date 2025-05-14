package packup.firebase.infra.config;

import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${files.firebase_admin_sdk_path}")
    private Resource resource;
    private static final String FIREBASE_APP_NAME = "packup";

    private static FirebaseApp firebaseApp;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        if (FirebaseApp.getApps().stream().noneMatch(app -> app.getName().equals(FIREBASE_APP_NAME))) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options, FIREBASE_APP_NAME);
        } else {
            firebaseApp = FirebaseApp.getInstance(FIREBASE_APP_NAME);
        }

        return firebaseApp;
    }


    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
