package johyoju04.concurcouponservice;

import johyoju04.concurcouponservice.model.entity.Coupon;
import johyoju04.concurcouponservice.model.entity.CouponGroup;
import johyoju04.concurcouponservice.model.entity.Member;
import johyoju04.concurcouponservice.model.entity.Promotion;
import johyoju04.concurcouponservice.model.mappedenum.DiscountType;
import johyoju04.concurcouponservice.model.mappedenum.ExclusiveType;
import johyoju04.concurcouponservice.model.mappedenum.MallType;
import johyoju04.concurcouponservice.repository.*;
import johyoju04.concurcouponservice.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

//@Transactional
@SpringBootTest
public class ConcurTest {
    private final CouponService couponService;
    private final MemberRepository memberRepository;
    private final PromotionRepository promotionRepository;
    private final CouponGroupRepository couponGroupRepository;
    private final CouponRepository couponRepository;

    private final MemberCouponRepository memberCouponRepository;
    private static final Clock NOW_CLOCK = Clock.fixed(Instant.parse("2024-01-02T10:00:00Z"), ZoneOffset.UTC);
    @SpyBean
    private Clock clock;

    @Autowired
    public ConcurTest(CouponService couponService
            , MemberRepository memberRepository
            , PromotionRepository promotionRepository
            , CouponGroupRepository couponGroupRepository
            , CouponRepository couponRepository
            , MemberCouponRepository memberCouponRepository) {
        this.couponService = couponService;
        this.memberRepository = memberRepository;
        this.promotionRepository = promotionRepository;
        this.couponGroupRepository = couponGroupRepository;
        this.couponRepository = couponRepository;
        this.memberCouponRepository = memberCouponRepository;
    }

    @BeforeEach
    void 초기데이터_생성() {
        LocalDateTime now = LocalDateTime.now();

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
                .initialQuantity(30)
                .remainQuantity(30)
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
    void 쿠폰_순차발급() {
        int memberCount = 50;

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();

        for (int i = 0; i < memberCount; i++) {
            Member member = Member.builder()
                    .email("test" + i + "@email.com")
                    .password("123")
                    .build();

            memberRepository.save(member);

            try {
                couponService.issueMemberCoupon(couponGroup.getId(), member.getId());
                successCount.incrementAndGet();
            } catch (Exception e) {
                failCount.incrementAndGet();
            }
        }
        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        Long memberCouponCnt = memberCouponRepository.countByCouponGroupId(1L);
        assertThat(memberCouponCnt)
                .isEqualTo(Math.min(memberCount, 30));
    }

    @Test
    void 쿠폰_동시_발급() throws InterruptedException {
        // given
        int memberCount = 50;
        int couponAmount = 30;
        CouponGroup couponGroup = couponGroupRepository.findById(1L).get();

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        // when
        for (int i = 0; i < memberCount; i++) {
            executorService.submit(() -> {
                try {
                    Member member = Member.builder()
                            .email("test" + "@email.com")
                            .password("123")
                            .build();

                    memberRepository.save(member);

                    couponService.issueMemberCoupon(couponGroup.getId(),member.getId());
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        System.out.println("successCount = " + successCount);
        System.out.println("failCount = " + failCount);

        // then
        Long memberCouponCnt = memberCouponRepository.countByCouponGroupId(1L);
        assertThat(memberCouponCnt)
                .isEqualTo(Math.min(memberCount, couponAmount));
    }
}
