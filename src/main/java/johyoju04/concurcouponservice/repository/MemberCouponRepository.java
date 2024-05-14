package johyoju04.concurcouponservice.repository;

import johyoju04.concurcouponservice.model.entity.MemberCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Long countByCouponGroupId(Long couponGroupId);

    boolean existsByMemberIdAndCouponGroupId(Long memberId, Long couponGroupId);

}
