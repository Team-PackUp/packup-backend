package packup.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import packup.chat.constant.ChatConstant;
import packup.chat.infra.RedisStreamListener;
import packup.chat.infra.RedisSubscriber;
import packup.common.util.UUIDGenerator;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {


    @Bean
    public ChannelTopic sendMessageTopic() {
        return new ChannelTopic(ChatConstant.REDIS_TOPIC_NAME);
    }

    @Bean
    public MessageListenerAdapter listenerAdapterSendMessage(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "sendMessage");
    }

    // Stream Container
    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamContainer(
            RedisConnectionFactory connectionFactory,
            RedisStreamListener listener) {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(1))
                        .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(connectionFactory, options);

        container.receive(Consumer.from(ChatConstant.CHAT_GROUP_NAME, UUIDGenerator.generate("consumer-")),
                StreamOffset.create(ChatConstant.CHAT_STREAM_KEY, ReadOffset.lastConsumed()),
                listener);

        container.start();

        return container;
    }

    // PUB/SUB 컨테이너
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapterSendMessage,
            ChannelTopic sendMessageTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterSendMessage, sendMessageTopic);
        return container;
    }


}