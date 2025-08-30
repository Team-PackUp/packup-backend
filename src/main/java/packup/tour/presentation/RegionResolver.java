package packup.tour.presentation;

import java.util.Optional;

public interface RegionResolver {
    Optional<String> resolveSidoName(double lat, double lng);
}