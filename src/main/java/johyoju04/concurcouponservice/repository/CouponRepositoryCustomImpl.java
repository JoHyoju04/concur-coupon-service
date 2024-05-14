package johyoju04.concurcouponservice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import johyoju04.concurcouponservice.model.entity.Coupon;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static johyoju04.concurcouponservice.model.entity.QCoupon.coupon;

@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Coupon> findByCouponGroupIdForUpdate(Long couponGroupId) {
        return Optional.ofNullable(queryFactory.selectFrom(coupon)
                .where(coupon.couponGroup.id.eq(couponGroupId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }
}
