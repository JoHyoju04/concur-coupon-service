package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.model.mappedenum.MallType;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "promotion")
public class Promotion extends AbstractTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Boolean isDisplay;

    private String bannerUrl;

    private String mainImageUrl;

    private MallType mallType;
}
