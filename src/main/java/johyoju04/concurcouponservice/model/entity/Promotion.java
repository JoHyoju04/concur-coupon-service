package johyoju04.concurcouponservice.model.entity;

import jakarta.persistence.*;
import johyoju04.concurcouponservice.model.mappedenum.MallType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "promotion")
public class Promotion extends AbstractTimeEntity{
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
