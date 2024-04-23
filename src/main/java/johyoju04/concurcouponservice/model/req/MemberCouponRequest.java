package johyoju04.concurcouponservice.model.req;

import jakarta.validation.constraints.NotNull;

public class MemberCouponRequest {
    @NotNull
    private Long promotionId;
    @NotNull
    private Long CouponGroupId;
}
