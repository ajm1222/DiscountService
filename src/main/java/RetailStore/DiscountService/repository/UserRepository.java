package RetailStore.DiscountService.repository;

import RetailStore.DiscountService.repository.model.UserType;
import RetailStore.DiscountService.repository.model.User;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {
    private Map<String, User> userMap = new HashMap<>() {
        {
            put("1", new User("1", "Riya", UserType.CUSTOMER, Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusYears(5).toInstant())));
            put("2", new User("2", "Dude", UserType.EMPLOYEE, Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusYears(5).toInstant())));
            put("3", new User("3", "Stud", UserType.AFFILIATE, Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusYears(5).toInstant())));
            put("4", new User("4", "Hunk", UserType.CUSTOMER, Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusYears(1).toInstant())));
        }
    };

    public User getUserById(String userId) {
        return userMap.getOrDefault(userId, null);
    }
}
