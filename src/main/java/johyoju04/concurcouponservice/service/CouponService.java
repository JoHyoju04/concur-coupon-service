package johyoju04.concurcouponservice.service;

import jakarta.transaction.Transactional;
import johyoju04.concurcouponservice.common.exception.BadRequestException;
import johyoju04.concurcouponservice.common.exception.ErrorCode;
import johyoju04.concurcouponservice.common.exception.NotFoundException;
import johyoju04.concurcouponservice.model.entity.Coupon;
import johyoju04.concurcouponservice.model.entity.CouponGroup;
import johyoju04.concurcouponservice.model.entity.Member;
import johyoju04.concurcouponservice.model.entity.MemberCoupon;
import johyoju04.concurcouponservice.model.mappedenum.MemberCouponState;
import johyoju04.concurcouponservice.repository.CouponGroupRepository;
import johyoju04.concurcouponservice.repository.CouponRepository;
import johyoju04.concurcouponservice.repository.MemberCouponRepository;
import johyoju04.concurcouponservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final MemberCouponRepository memberCouponRepository;
    private final CouponGroupRepository couponGroupRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    @Transactional
    public void issueMemberCoupon(Long couponGroupId, Long memberId) {
        //쿠폰 그룹의 발급 가능여부 조회
        CouponGroup couponGroup = findCouponGroupById(couponGroupId);
        Member member = findMemberById(memberId);
        LocalDateTime now = LocalDateTime.now(clock);

        couponGroup.validateIssue(now);

        validateAlreadyIssued(memberId, couponGroupId);

        //쿠폰의 잔여수량 감소
        //랜덤이 아닐 경우
        Coupon coupon = findCouponByCouponGroupId(couponGroupId);
        coupon.decreaseRemainQuantity();

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .couponGroup(couponGroup)
                .member(member)
                .issuedAt(now)
                .state(MemberCouponState.BEFORE_USAGE)
                .build();

        memberCouponRepository.save(memberCoupon);
    }

    private CouponGroup findCouponGroupById(Long couponGroupId) {
        return couponGroupRepository.findById(couponGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_GROUP_NOT_FOUND));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void validateAlreadyIssued(Long memberId, Long couponGroupId) {
        //member가 쿠폰 발급받은지 확인하기
        if (memberCouponRepository.existsByMemberIdAndCouponGroupId(memberId, couponGroupId)) {
            throw new BadRequestException(ErrorCode.COUPON_OVER_AMOUNT_PER_MEMBER);
        }

    }

    private Coupon findCouponByCouponGroupId(Long couponGroupId) {
        return couponRepository.findByCouponGroupIdForUpdate(couponGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND));
    }
}
