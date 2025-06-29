package packup.recommend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import packup.recommend.domain.Recommend;

import java.util.List;
import java.util.Map;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    @Query(
            value = """
SELECT tour_seq,
           COUNT(*) AS cnt
  FROM recommend
 WHERE tour_seq IN (:tourSeq)
   AND action_type = '000011'
  GROUP BY tour_seq
  ORDER BY cnt DESC
  LIMIT :count
  """, nativeQuery = true)
    List<Map<String, Object>> findTopPopularTour(@Param("tourSeq") List<Long> seq, @Param("count") int count);

}
