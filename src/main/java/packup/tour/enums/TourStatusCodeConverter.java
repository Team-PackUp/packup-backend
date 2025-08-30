package packup.tour.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TourStatusCodeConverter implements AttributeConverter<TourStatusCode, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TourStatusCode attribute) {
        if (attribute == null) return null;
        try {
            return Integer.parseInt(attribute.getCode());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid TourStatusCode code: " + attribute.getCode(), e);
        }
    }

    @Override
    public TourStatusCode convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        return TourStatusCode.fromCode(String.valueOf(dbData));
    }
}