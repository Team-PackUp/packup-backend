package packup.tour.enums;

import lombok.Getter;

import java.util.*;

@Getter
public enum KrSidoCode {
    SEOUL   (11L, "서울특별시",      "서울", "서울시"),
    BUSAN   (26L, "부산광역시",      "부산", "부산시"),
    DAEGU   (27L, "대구광역시",      "대구", "대구시"),
    INCHEON (28L, "인천광역시",      "인천", "인천시"),
    GWANGJU (29L, "광주광역시",      "광주", "광주시"),
    DAEJEON (30L, "대전광역시",      "대전", "대전시"),
    ULSAN   (31L, "울산광역시",      "울산", "울산시"),
    SEJONG  (36L, "세종특별자치시",   "세종", "세종시"),
    GYEONGGI(41L, "경기도",          "경기"),
    GANGWON (42L, "강원특별자치도",   "강원도", "강원"),
    CHUNGBUK(43L, "충청북도",        "충북"),
    CHUNGNAM(44L, "충청남도",        "충남"),
    JEONBUK (45L, "전북특별자치도",   "전라북도", "전북"),
    JEONNAM (46L, "전라남도",        "전남"),
    GYEONGBUK(47L,"경상북도",        "경북"),
    GYEONGNAM(48L,"경상남도",        "경남"),
    JEJU    (50L, "제주특별자치도",   "제주도", "제주");

    private final Long code;
    private final String officialName;
    private final List<String> aliases;

    KrSidoCode(Long code, String officialName, String... aliases) {
        this.code = code;
        this.officialName = officialName;
        this.aliases = Arrays.asList(aliases);
    }

    // "서울특별시" / "서울시" / "서울" 등 정확 매칭
    public static Optional<KrSidoCode> fromName(String name) {
        if (name == null || name.isBlank()) return Optional.empty();
        final String n = norm(name);
        for (KrSidoCode s : values()) {
            if (n.equals(norm(s.officialName))) return Optional.of(s);
            for (String a : s.aliases) {
                if (n.equals(norm(a))) return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    // 긴 주소 문자열 안에서 부분 포함 매칭(예: "서울특별시 중구 세종대로 …")
    public static Optional<KrSidoCode> fromText(String text) {
        if (text == null || text.isBlank()) return Optional.empty();
        final String n = norm(text);
        for (KrSidoCode s : values()) {
            if (n.contains(norm(s.officialName))) return Optional.of(s);
            for (String a : s.aliases) {
                if (n.contains(norm(a))) return Optional.of(s);
            }
        }
        return Optional.empty();
    }

    private static String norm(String s) {
        if (s == null) return "";
        String t = s.toLowerCase(Locale.KOREAN)
                .replaceAll("\\s+", "")
                .replace("광역시","")
                .replace("특별시","")
                .replace("특별자치시","")
                .replace("특별자치도","")
                .replace("자치도","")
                .replace("자치시","")
                .replace("시","")
                .replace("도","");
        return t;
    }
}
