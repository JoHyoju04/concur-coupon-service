package johyoju04.concurcouponservice.repository;

import johyoju04.concurcouponservice.model.entity.CouponGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponGroupRepository extends JpaRepository<CouponGroup,Long> {
}
