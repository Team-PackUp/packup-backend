package packup.chat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SocketHandler implements WebSocketHandler {

    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;

    public SocketHandler(RedisPublisher redisPublisher, RedisSubscriber redisSubscriber) {
        this.redisPublisher = redisPublisher;
        this.redisSubscriber = redisSubscriber;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<String> incoming = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> redisPublisher.publish("chatroom1", message)); // Redis 발행

        Flux<WebSocketMessage> outgoing = redisSubscriber.getMessages()
                .map(session::textMessage); // Redis 수신

        return session.send(outgoing).and(incoming);
    }
}
