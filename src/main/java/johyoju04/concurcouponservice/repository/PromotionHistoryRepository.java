package johyoju04.concurcouponservice.repository;

import johyoju04.concurcouponservice.model.entity.PromotionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionHistoryRepository extends JpaRepository<PromotionHistory,Long> {
}
