package how.wow.domain.enums;

public enum EmoteType {
    SMILE(":)"),
    BIG_SMILE(":-)"),
    LAUGH(":D"),
    BIG_LAUGH(":-D"),
    WINK(";)"),
    BIG_WINK(";-)"),
    TONGUE(":P"),
    BIG_TONGUE(":-P"),
    SAD(":("),
    BIG_SAD(":-("),
    CRY(":'("),
    CRY_ALT("T_T"),
    ANGRY(">:("),
    ANGRY_ALT(">:-("),
    SURPRISE(":O"),
    BIG_SURPRISE(":-O"),
    NEUTRAL(":|"),
    BIG_NEUTRAL(":-|"),
    CONFUSED("o_O"),
    KISS(":*"),
    BIG_KISS(":-*"),
    SHRUG("¯\\_(ツ)_/¯"),
    CAT("(=^..^=)"),
    DOG("(ᵔᴥᵔ)"),
    OWL("(0,0)"),
    FISH("><>"),
    SPIDER("/\\(00)/\\");

    private final String textRepresentation;

    EmoteType(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    public String getText() {
        return textRepresentation;
    }

    public static EmoteType fromText(String text) {
        for (EmoteType emote : values()) {
            if (emote.toString().equalsIgnoreCase(text)) {
                return emote;
            }
        }
        return null;
    }

    public static boolean containsEmote(String emote) {
        for (EmoteType e : values()) {
            if (emote.equalsIgnoreCase(e.toString())) {
                return true;
            }
        }
        return false;
    }

    public static String getEmotes() {
        StringBuilder emotes = new StringBuilder();
        for (EmoteType emote : values()) {
            emotes.append(emote.toString()).append(" -> ")
                    .append(emote.textRepresentation).append(",\n");
        }
        if (!emotes.isEmpty()) {
            emotes.setLength(emotes.length() - 2);
        }
        return emotes.toString().trim();
    }
}