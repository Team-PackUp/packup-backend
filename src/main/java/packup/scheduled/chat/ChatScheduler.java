package packup.scheduled.chat;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import packup.chat.service.ChatService;

@Component
@AllArgsConstructor
public class ChatScheduler {

    private final ChatService chatService;

    // 매일 00시에 실행
//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void createChatRoom() {

        chatService.createChatRoom();
    }

}
