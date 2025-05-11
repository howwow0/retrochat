package how.wow.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    public static final User SYSTEM = new User("system", "System", Instant.now());
    public static final User CLIENT = new User("client", "Client", Instant.now());
    @EqualsAndHashCode.Include
    private final String id;
    @Setter
    private String nickname;
    private final Instant joinTime;

    public User(String id, String nickname, Instant joinTime) {
        this.id = Objects.requireNonNull(id);
        this.nickname = Objects.requireNonNull(nickname);
        this.joinTime = Objects.requireNonNull(joinTime);
    }
}