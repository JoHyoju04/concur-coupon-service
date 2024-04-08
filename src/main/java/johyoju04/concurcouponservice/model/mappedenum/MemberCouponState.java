package johyoju04.concurcouponservice.model.mappedenum;

import lombok.Getter;

@Getter
public enum MemberCouponState {
    //사용전
    BEFORE_USAGE,

    //사용후
    AFTER_USAGE,

    //만료
    EXPIRED
}
