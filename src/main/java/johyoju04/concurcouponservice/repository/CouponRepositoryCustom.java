package johyoju04.concurcouponservice.repository;

import johyoju04.concurcouponservice.model.entity.Coupon;

import java.util.Optional;

public interface CouponRepositoryCustom {
    Optional<Coupon> findByCouponGroupIdForUpdate(Long couponGroupId);
}
