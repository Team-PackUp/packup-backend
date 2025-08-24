package packup.user.dto;


public record UserPushTarget(Long userSeq, String nickname, String profileImagePath, String fcmToken) {}
