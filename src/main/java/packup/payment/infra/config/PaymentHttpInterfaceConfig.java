package packup.payment.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import packup.payment.infra.client.TossPaymentClient;

@Configuration
public class PaymentHttpInterfaceConfig {

    @Bean
    public TossPaymentClient tossPaymentClient() {
        return createHttpInterface(TossPaymentClient.class);
    }

    private <T> T createHttpInterface(Class<T> clazz) {
        WebClient webClient = WebClient.builder().
                baseUrl("https://api.tosspayments.com")
                .build();

        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(clazz);
    }
}

