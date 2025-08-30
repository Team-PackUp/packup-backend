package packup.tour.enums;


import java.util.*;
import java.util.stream.Stream;

public enum KrSidoCode {
    SEOUL   (11L, "서울특별시", "서울", "서울시", "seoul"),
    BUSAN   (26L, "부산광역시", "부산", "부산시", "busan"),
    DAEGU   (27L, "대구광역시", "대구", "대구시", "daegu"),
    INCHEON (28L, "인천광역시", "인천", "인천시", "incheon"),
    GWANGJU (29L, "광주광역시", "광주", "광주시", "gwangju"),
    DAEJEON (30L, "대전광역시", "대전", "대전시", "daejeon"),
    ULSAN   (31L, "울산광역시", "울산", "울산시", "ulsan"),
    SEJONG  (36L, "세종특별자치시", "세종", "세종시", "sejong"),

    GYEONGGI (41L, "경기도", "경기", "gyeonggi", "gyeonggi-do"),
    GANGWON  (42L, "강원특별자치도", "강원도", "강원", "gangwon", "gangwon-do"),
    CHUNGBUK (43L, "충청북도", "충북", "chungbuk", "chungcheongbuk-do"),
    CHUNGNAM (44L, "충청남도", "충남", "chungnam", "chungcheongnam-do"),
    JEONBUK  (45L, "전북특별자치도", "전라북도", "전북", "jeonbuk", "jeollabuk-do"),
    JEONNAM  (46L, "전라남도", "전남", "jeonnam", "jeollanam-do"),
    GYEONGBUK(47L, "경상북도", "경북", "gyeongbuk", "gyeongsangbuk-do"),
    GYEONGNAM(48L, "경상남도", "경남", "gyeongnam", "gyeongsangnam-do"),
    JEJU     (50L, "제주특별자치도", "제주도", "제주", "jeju", "jeju-do");

    private final Long code;
    private final String officialName;
    private final Set<String> aliases;

    KrSidoCode(Long code, String officialName, String... aliases) {
        this.code = code;
        this.officialName = officialName;
        // 인덱싱 시 normalization 적용
        Set<String> set = new HashSet<>();
        set.add(officialName);
        Collections.addAll(set, aliases);
        this.aliases = Collections.unmodifiableSet(set);
    }

    public Long getCode() { return code; }
    public String getOfficialName() { return officialName; }

    private static final Map<String, KrSidoCode> INDEX;
    static {
        Map<String, KrSidoCode> m = new HashMap<>();
        for (KrSidoCode s : values()) {
            Stream.concat(Stream.of(s.officialName), s.aliases.stream())
                    .map(KrSidoCode::norm)
                    .forEach(k -> m.putIfAbsent(k, s));
        }
        INDEX = Collections.unmodifiableMap(m);
    }

    private static String norm(String s) {
        if (s == null) return "";
        String t = s.toLowerCase(Locale.ROOT).replaceAll("\\s+", "");
        t = t.replace("특별자치도", "")
                .replace("특별자치시", "")
                .replace("특별시", "")
                .replace("광역시", "")
                .replace("자치시", "")
                .replace("자치도", "")
                .replace("도", "")
                .replace("시", "");
        return t;
    }

    public static Optional<KrSidoCode> fromName(String input) {
        KrSidoCode found = INDEX.get(norm(input));
        return Optional.ofNullable(found);
    }
}
