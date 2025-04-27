package packup.common.util;

import java.util.UUID;

public class UUIDGenerator {

    public static String generate(String baseNickname) {
        String randomSuffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return baseNickname + "_" + randomSuffix;
    }
}
