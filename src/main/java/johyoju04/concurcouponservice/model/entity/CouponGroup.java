package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.model.mappedenum.ExclusiveType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_group")
public class CouponGroup extends AbstractTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="promotion_id")
    Promotion promotion;

    private ExclusiveType exclusiveType;

    private LocalDateTime usageStartedAt;

    private LocalDateTime usagefinishedAt;

    private LocalDateTime issueStartedAt;

    private LocalDateTime issueFinishedAt;

    private Boolean isRandom;

    private Boolean isIssued;
}
