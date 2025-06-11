package packup.tour.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TourStatusCodeConverter implements AttributeConverter<TourStatusCode, String> {

    @Override
    public String convertToDatabaseColumn(TourStatusCode attribute) {
        return attribute.getCode();
    }

    @Override
    public TourStatusCode convertToEntityAttribute(String dbData) {
        return TourStatusCode.fromCode(dbData);
    }
}
