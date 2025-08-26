package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import packup.chat.dto.ChatRoomResponse;
import packup.chat.dto.RoomChangedEvent;

@Component
@RequiredArgsConstructor
public class RoomChangedEventHandler {

    private final SimpMessagingTemplate messaging;

    @EventListener
    public void on(RoomChangedEvent e) {

        for (Long uid : e.targetUserIds()) {
            System.out.println(uid);
            ChatRoomResponse dto = e.userSpecificDTO().get(uid);
            messaging.convertAndSendToUser(
                    uid.toString(),
                    "/queue/chatroom-refresh",
                    dto
            );
        }
    }
}
