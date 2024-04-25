package johyoju04.concurcouponservice.service;

import jakarta.transaction.Transactional;
import johyoju04.concurcouponservice.common.exception.BadRequestException;
import johyoju04.concurcouponservice.common.exception.ErrorCode;
import johyoju04.concurcouponservice.common.exception.NotFoundException;
import johyoju04.concurcouponservice.model.entity.Coupon;
import johyoju04.concurcouponservice.model.entity.CouponGroup;
import johyoju04.concurcouponservice.model.entity.Member;
import johyoju04.concurcouponservice.model.entity.Promotion;
import johyoju04.concurcouponservice.model.mappedenum.DiscountType;
import johyoju04.concurcouponservice.model.mappedenum.ExclusiveType;
import johyoju04.concurcouponservice.model.mappedenum.MallType;
import johyoju04.concurcouponservice.repository.CouponGroupRepository;
import johyoju04.concurcouponservice.repository.CouponRepository;
import johyoju04.concurcouponservice.repository.MemberRepository;
import johyoju04.concurcouponservice.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class CouponServiceTest {
    private final CouponService couponService;
    private final MemberRepository memberRepository;
    private final PromotionRepository promotionRepository;
    private final CouponGroupRepository couponGroupRepository;
    private final CouponRepository couponRepository;

    @Autowired
    public CouponServiceTest(CouponService couponService, MemberRepository memberRepository
            , PromotionRepository promotionRepository, CouponGroupRepository couponGroupRepository, CouponRepository couponRepository) {
        this.couponService = couponService;
        this.memberRepository = memberRepository;
        this.promotionRepository = promotionRepository;
        this.couponGroupRepository = couponGroupRepository;
        this.couponRepository = couponRepository;
    }

    @BeforeEach
    void 초기데이터_생성() {
        LocalDateTime now = LocalDateTime.now();
        Member member = Member.builder()
                .email("test@email.com")
                .password("123")
                .build();

        memberRepository.save(member);

        Promotion promotion = Promotion.builder()
                .bannerUrl("test banner")
                .mainImageUrl("test main image")
                .description("test description")
                .name("test promotion")
                .finishedAt(now.plusDays(1))
                .startedAt(now)
                .mallType(MallType.OFFLINE)
                .isDisplay(true)
                .build();

        promotionRepository.save(promotion);

        CouponGroup couponGroup = CouponGroup.builder()
                .exclusiveType(ExclusiveType.APP)
                .promotion(promotion)
                .issuedStartedAt(now)
                .issuedFinishedAt(now.plusDays(7))
                .usageStartedAt(now)
                .usageFinishedAt(now.plusDays(30))
                .isIssued(true)
                .isRandom(false)
                .build();

        couponGroupRepository.save(couponGroup);

        Coupon coupon = Coupon.builder()
                .couponGroup(couponGroup)
                .discountType(DiscountType.PERCENTAGE)
                .name("test coupon")
                .initialQuantity(100)
                .remainQuantity(100)
                .maxDiscountAmount(BigDecimal.valueOf(100))
                .percentageDiscountRate(BigDecimal.valueOf(10))
                .minPurchaseAmount(BigDecimal.valueOf(100))
                .build();

        couponRepository.save(coupon);

    }

    @Test
    void 회원인_멤버_쿠폰_발급() {
        Optional<Member> member = Optional.ofNullable(memberRepository.findById(1L).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND)));
        Optional<CouponGroup> couponGroup = Optional.ofNullable(couponGroupRepository.findById(1L).orElseThrow(() -> new NotFoundException(ErrorCode.COUPON_NOT_FOUND)));
        couponService.issueMemberCoupon(couponGroup.get().getId(), member.get().getId());
    }

    @Test
    void 회원이_아닌_멤버_쿠폰_발급() {
        Optional<CouponGroup> couponGroup = couponGroupRepository.findById(1L);
        assertThatThrownBy(() -> couponService.issueMemberCoupon(couponGroup.get().getId(), 2L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 쿠폰그룹_수량초과로_발급불가능() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        couponGroup.updateIsIssued(false);
        //테스트 코드 내에서 Dirty check 안된다.
        couponGroupRepository.save(couponGroup);
        assertThatThrownBy(() -> couponService.issueMemberCoupon(couponGroup.getId(), 1L))
                .isInstanceOf(BadRequestException.class);

    }

    @Test
    void 쿠폰그룹_발급_불가능_날짜() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        couponGroup.updateIssuedDate(LocalDateTime.now().minusDays(7), LocalDateTime.now().minusDays(3));

        //테스트 코드 내에서 Dirty check 안된다.
        couponGroupRepository.save(couponGroup);
        assertThatThrownBy(() -> couponService.issueMemberCoupon(couponGroup.getId(), 1L))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 쿠폰그룹_발급_가능_날짜() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        LocalDateTime now = LocalDateTime.of(2024, 12, 01, 14, 00);
        couponGroup.updateIssuedDate(now, now.plusDays(3));

        //테스트 코드 내에서 Dirty check 안된다.
        couponGroupRepository.save(couponGroup);
        couponService.issueMemberCoupon(couponGroup.getId(), 1L);
    }

    @Test
    void 멤버가_이미_쿠폰발급() {

    }

    @Test
    void 쿠폰_수량초과() {

    }
}
