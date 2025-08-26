package packup.common.util;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import packup.common.enums.YnType;
import packup.user.domain.repository.UserInfoRepository;

import java.util.Collections;
import java.util.List;


@Component
@AllArgsConstructor
public class ValidationUtil {

    private final UserInfoRepository userInfoRepository;
    public List<Long> validateActiveUser(List<Long> userSeq) {
        if (userSeq.isEmpty()) return Collections.emptyList();

        return userInfoRepository.findActiveUserSeq(userSeq, YnType.N, YnType.N);
    }
}
