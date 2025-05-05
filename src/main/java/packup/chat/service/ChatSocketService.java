package packup.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import packup.chat.dto.ChatMessageDTO;
import packup.chat.infra.RedisPublisher;

@Service
@RequiredArgsConstructor
public class ChatSocketService {

    private final RedisPublisher redisPublisher; // Redis Pub/Sub 발행 담당
    // Redis Stream 저장 등에 사용

    private final RedisService redisService;

    @Transactional
    public void sendChatMessage(ChatMessageDTO chatMessageDto) {
        try {
            // pub
            redisPublisher.publishMessage(chatMessageDto);

            // 채팅방 최신화
            redisPublisher.publishChatRoomList(chatMessageDto);

            // stream
            redisService.appendToStream(chatMessageDto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

