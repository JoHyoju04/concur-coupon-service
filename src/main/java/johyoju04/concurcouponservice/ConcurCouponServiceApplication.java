package johyoju04.concurcouponservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ConcurCouponServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConcurCouponServiceApplication.class, args);
    }

}
