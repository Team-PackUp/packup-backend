package packup.storage.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsS3Config {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(AwsProperties props) {
        var cred = props.getCredentials();
        boolean hasStatic = cred.getAccessKeyId() != null && !cred.getAccessKeyId().isBlank()
                && cred.getSecretAccessKey() != null && !cred.getSecretAccessKey().isBlank();

        if (!hasStatic) {
            return DefaultCredentialsProvider.create();
        }
        if (cred.getSessionToken() != null && !cred.getSessionToken().isBlank()) {
            var s = AwsSessionCredentials.create(
                    cred.getAccessKeyId(), cred.getSecretAccessKey(), cred.getSessionToken());
            return StaticCredentialsProvider.create(s);
        }
        var b = AwsBasicCredentials.create(cred.getAccessKeyId(), cred.getSecretAccessKey());
        return StaticCredentialsProvider.create(b);
    }

    @Bean
    public S3Presigner s3Presigner(AwsProperties props, AwsCredentialsProvider provider) {
        return S3Presigner.builder()
                .region(Region.of(props.getRegion()))
                .credentialsProvider(provider)
                .build();
    }
}
