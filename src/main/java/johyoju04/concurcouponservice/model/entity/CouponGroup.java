package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.common.exception.BadRequestException;
import johyoju04.concurcouponservice.common.exception.ErrorCode;
import johyoju04.concurcouponservice.model.mappedenum.ExclusiveType;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "coupon_group")
public class CouponGroup extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    Promotion promotion;

    private ExclusiveType exclusiveType;

    private LocalDateTime usageStartedAt;

    private LocalDateTime usageFinishedAt;

    private LocalDateTime issuedStartedAt;

    private LocalDateTime issuedFinishedAt;

    private Boolean isRandom;

    private Boolean isIssued;

    public void validateIssuedDate(LocalDateTime now) {
        if (!(issuedStartedAt.isBefore(now) && issuedFinishedAt.isAfter(now))) {
            throw new BadRequestException(ErrorCode.NOT_ISSUED_TIME);
        }
    }

    public void updateIsIssued(Boolean isIssued) {
        if (isIssued == null) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
        this.isIssued = isIssued;
    }

    public void updateIssuedDate(LocalDateTime issuedStartedAt, LocalDateTime issuedFinishedAt) {
        if (issuedStartedAt == null || issuedFinishedAt == null || issuedStartedAt.isAfter(issuedFinishedAt)) {
            throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
        }
        this.issuedStartedAt = issuedStartedAt;
        this.issuedFinishedAt = issuedFinishedAt;
    }

}
