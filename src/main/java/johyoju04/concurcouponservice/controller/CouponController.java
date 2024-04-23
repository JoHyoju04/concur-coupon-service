package johyoju04.concurcouponservice.controller;

import johyoju04.concurcouponservice.common.ApiResponse;
import johyoju04.concurcouponservice.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    //쿠폰발급
    @PostMapping("/v1/coupon-groups/{couponGroupId}/member/{memberId}/members-coupons")
    public ApiResponse<Object> issueCoupon(
            @PathVariable("couponGroupId") Long couponGroupId,
            @PathVariable("memberId") Long memberId
            ){
        couponService.issueMemberCoupon(couponGroupId,memberId);

        return ApiResponse.success();
    }
}
