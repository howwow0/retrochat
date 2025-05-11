package how.wow.domain.model;


import java.time.Instant;
import java.util.Objects;

public record Message(User author, String text, Instant timestamp) {
    public Message(User author, String text, Instant timestamp) {
        this.author = Objects.requireNonNull(author);
        this.text = Objects.requireNonNull(text);
        this.timestamp = Objects.requireNonNull(timestamp);
    }
}