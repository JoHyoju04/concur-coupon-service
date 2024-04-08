package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.model.mappedenum.MemberCouponState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_coupon")
public class MemberCoupon extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="coupon_id")
    Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    Member member;

    private LocalDateTime issuedAt;

    private LocalDateTime usedAt;

    private MemberCouponState state;
}
