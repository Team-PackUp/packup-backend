package packup.recommendation.domain;

import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JpaDataModelBuilder {

    public static DataModel buildPreferences(List<Recommend> preferences) {
        FastByIDMap<PreferenceArray> preferenceMap = new FastByIDMap<>();

        Map<Long, List<Recommend>> group = preferences.stream()
                .collect(Collectors.groupingBy(Recommend::getUserSeq));

        for (Map.Entry<Long, List<Recommend>> entry : group.entrySet()) {
            long userSeq = entry.getKey();
            List<Recommend> prefs = entry.getValue();

            GenericUserPreferenceArray array = new GenericUserPreferenceArray(prefs.size());
            for (int i = 0; i < prefs.size(); i++) {
                Recommend p = prefs.get(i);
                array.setUserID(i, userSeq);
                array.setItemID(i, p.getTourSeq());
                array.setValue(i, p.getScore());
            }

            preferenceMap.put(userSeq, array);
        }

        return new GenericDataModel(preferenceMap);
    }
}
