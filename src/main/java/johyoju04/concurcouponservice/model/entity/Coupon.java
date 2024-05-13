package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.common.exception.BadRequestException;
import johyoju04.concurcouponservice.common.exception.ErrorCode;
import johyoju04.concurcouponservice.model.mappedenum.DiscountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_group_id")
    CouponGroup couponGroup;

    private DiscountType discountType;

    //정액 할인 금액 (정액쿠폰, 단위 : 원)
    private BigDecimal fixedDiscountAmount;

    //정률 할인률 (정률쿠폰, 단위 : %)
    private BigDecimal percentageDiscountRate;

    //최소 구매 금액
    private BigDecimal minPurchaseAmount;

    //최대 할인 금액
    private BigDecimal maxDiscountAmount;

    private String name;

    private Integer initialQuantity;

    private Integer remainQuantity;

    public void decreaseRemainQuantity() {
        if (this.remainQuantity <= 0) {
            throw new BadRequestException(ErrorCode.COUPON_OVER_AMOUNT);
        }
        remainQuantity--;
    }
}
