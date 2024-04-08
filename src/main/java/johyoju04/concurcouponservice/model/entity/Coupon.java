package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.model.mappedenum.DiscountType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon")
public class Coupon extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="coupon_group_id")
    CouponGroup couponGroup;

    private DiscountType discountType;

    //정액 할인 금액 (정액쿠폰, 단위 : 원)
    private BigDecimal fixedDiscountAmount;

    //정률 할인률 (정률쿠폰, 단위 : %)
    private BigDecimal percentDiscountRate;

    //최소 구매 금액
    private BigDecimal minPurchaseAmount;

    //최대 할인 금액
    private BigDecimal maxDiscountAmount;

    private String name;

    private Integer initialQuantity;

    private Integer remainQuantity;
}
