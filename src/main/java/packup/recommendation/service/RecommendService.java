package packup.recommendation.service;

import lombok.RequiredArgsConstructor;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.stereotype.Service;
import packup.recommendation.domain.JpaDataModelBuilder;
import packup.recommendation.domain.Recommend;
import packup.recommendation.domain.repository.RecommendRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository preferenceRepository;

    public List<RecommendedItem> recommendForUser(long userId, int count) throws TasteException {
        List<Recommend> preferences = preferenceRepository.findAll();
        DataModel model = JpaDataModelBuilder.buildPreferences(preferences);

        ItemSimilarity similarity = new UncenteredCosineSimilarity(model);
        GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(model, similarity);

        return recommender.recommend(userId, count, rescorer());
    }

    private IDRescorer rescorer() {
        return new IDRescorer() {
            @Override
            public double rescore(long id, double originalScore) {
                return originalScore;
            }

            @Override
            public boolean isFiltered(long id) {
                return false;
            }
        };
    }

}
