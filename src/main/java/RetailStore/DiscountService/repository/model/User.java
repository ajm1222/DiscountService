package RetailStore.DiscountService.repository.model;

import lombok.Data;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
public class User {
    private final String id;
    private final String name;
    private final UserType userType;
    private final Date dateOfJoining;

    public Integer getDiscountPercentage() {
        int discount = 0;
        switch (this.userType) {
            case AFFILIATE -> discount = 10;
            case EMPLOYEE -> discount = 30;
            case CUSTOMER -> discount = Period.between(
                    LocalDate.from(ZonedDateTime.now(ZoneOffset.UTC)),
                    dateOfJoining.toInstant().atZone(ZoneOffset.UTC).toLocalDate()).getYears() >= 2 ? 5: 0;
        }
        return discount;
    }
}
