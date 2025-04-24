package packup.chat.config;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;

import java.nio.charset.StandardCharsets;

@Component
public class RedisSubscriber implements MessageListener {
    private final FluxProcessor<String, String> processor = DirectProcessor.create();
    private final FluxSink<String> sink = processor.sink();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        sink.next(msg); // 수신된 메시지를 Flux로 푸시
    }

    public Flux<String> getMessages() {
        return processor;
    }
}

