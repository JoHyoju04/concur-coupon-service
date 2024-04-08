package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "promotion_history")
public class PromotionHistory extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime participatedAt;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="coupon_group_id")
    CouponGroup couponGroup;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="promotion_id")
    Promotion promotion;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="member_id")
    Member member;


}
