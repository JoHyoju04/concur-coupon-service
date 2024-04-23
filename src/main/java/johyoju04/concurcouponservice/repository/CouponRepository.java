package johyoju04.concurcouponservice.repository;

import johyoju04.concurcouponservice.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponGroupId(Long couponGroupId);
}
