package packup.chat.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import packup.chat.dto.RoomChangedEvent;

@Component
@RequiredArgsConstructor
public class RoomChangedEventHandler {

    private final SimpMessagingTemplate messaging;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(RoomChangedEvent e) {

        var signal = new RoomChangeSignal(e.chatRoomSeq());
        for (Long uid : e.targetUserIds()) {
            messaging.convertAndSendToUser(
                    uid.toString(),
                    "/queue/chatroom-refresh",
                    signal
            );
        }
    }
}
