package johyoju04.concurcouponservice.service;

import jakarta.transaction.Transactional;
import johyoju04.concurcouponservice.common.exception.ErrorCode;
import johyoju04.concurcouponservice.common.exception.NotFoundException;
import johyoju04.concurcouponservice.model.entity.*;
import johyoju04.concurcouponservice.model.mappedenum.DiscountType;
import johyoju04.concurcouponservice.model.mappedenum.ExclusiveType;
import johyoju04.concurcouponservice.model.mappedenum.MallType;
import johyoju04.concurcouponservice.model.mappedenum.MemberCouponState;
import johyoju04.concurcouponservice.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@Transactional
@SpringBootTest
public class CouponServiceTest {
    private final CouponService couponService;
    private final MemberRepository memberRepository;
    private final PromotionRepository promotionRepository;
    private final CouponGroupRepository couponGroupRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;
    private static final LocalDateTime starndardLocalDateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final Clock NOW_CLOCK = Clock.fixed(Instant.parse("2024-01-02T10:00:00Z"), ZoneOffset.UTC);
    @SpyBean
    private Clock clock;

    @Autowired
    public CouponServiceTest(CouponService couponService, MemberRepository memberRepository
            , PromotionRepository promotionRepository, CouponGroupRepository couponGroupRepository
            , CouponRepository couponRepository, MemberCouponRepository memberCouponRepository) {
        this.couponService = couponService;
        this.memberRepository = memberRepository;
        this.promotionRepository = promotionRepository;
        this.couponGroupRepository = couponGroupRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @BeforeEach
    void 초기데이터_생성() {
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
                .finishedAt(starndardLocalDateTime.plusDays(7))
                .startedAt(starndardLocalDateTime)
                .mallType(MallType.OFFLINE)
                .isDisplay(true)
                .build();

        promotionRepository.save(promotion);

        CouponGroup couponGroup = CouponGroup.builder()
                .exclusiveType(ExclusiveType.APP)
                .promotion(promotion)
                .issuedStartedAt(starndardLocalDateTime)
                .issuedFinishedAt(starndardLocalDateTime.plusDays(7))
                .usageStartedAt(starndardLocalDateTime)
                .usageFinishedAt(starndardLocalDateTime.plusDays(30))
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

        doReturn(Instant.now(NOW_CLOCK))
                .when(clock)
                .instant();

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
                .hasMessage(ErrorCode.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 수량초과로_발급불가능한_쿠폰그룹() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        couponGroup.updateIsIssued(false);
        //테스트 코드 내에서 Dirty check 안된다.
        couponGroupRepository.save(couponGroup);

        assertThatThrownBy(() -> couponService.issueMemberCoupon(couponGroup.getId(), 1L))
                .hasMessage(ErrorCode.COUPON_OVER_AMOUNT.getMessage());

    }

    @Test
    void 발급_불가능한_날짜인_쿠폰그룹() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();

        assertThatThrownBy(() -> couponGroup.validateIssue(starndardLocalDateTime.minusDays(1)))
                .hasMessage(ErrorCode.NOT_ISSUED_TIME.getMessage());
    }

    @Test
    void 발급_가능한_날짜인_쿠폰그룹() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        LocalDateTime originIssuedStartedAt = couponGroup.getIssuedStartedAt();
        couponGroup.updateIssuedDate(originIssuedStartedAt.plusDays(1), originIssuedStartedAt.plusDays(2));

        couponGroupRepository.save(couponGroup);

        //테스트 코드 내에서 Dirty check 안된다.
        couponGroupRepository.save(couponGroup);

        couponService.issueMemberCoupon(couponGroup.getId(), 1L);
    }

    @Test
    void 멤버가_이미_쿠폰발급() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        Coupon coupon = couponRepository.findById(1L).get();
        Member member = memberRepository.findById(1L).get();
        LocalDateTime issuedDate = starndardLocalDateTime.plusDays(1);

        MemberCoupon memberCoupon = MemberCoupon.builder()
                .coupon(coupon)
                .couponGroup(couponGroup)
                .member(member)
                .issuedAt(issuedDate)
                .state(MemberCouponState.BEFORE_USAGE)
                .build();

        memberCouponRepository.save(memberCoupon);

        assertThatThrownBy(() -> couponService.issueMemberCoupon(couponGroup.getId(), member.getId()))
                .hasMessage(ErrorCode.COUPON_OVER_AMOUNT_PER_MEMBER.getMessage());
    }

    @Test
    void 쿠폰_수량초과() {
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();
        Coupon coupon = couponRepository.findById(1L).get();

        Integer remainQuantity = 0;
        Coupon updateCoupon = Coupon.builder()
                .id(coupon.getId())
                .couponGroup(couponGroup)
                .discountType(coupon.getDiscountType())
                .name(coupon.getName())
                .initialQuantity(coupon.getInitialQuantity())
                .remainQuantity(remainQuantity)
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .percentageDiscountRate(coupon.getPercentageDiscountRate())
                .minPurchaseAmount(coupon.getMinPurchaseAmount())
                .build();

        couponRepository.save(updateCoupon);

        assertThatThrownBy(() -> couponService.issueMemberCoupon(couponGroup.getId(), 1L))
                .hasMessage(ErrorCode.COUPON_OVER_AMOUNT.getMessage());
    }
}
