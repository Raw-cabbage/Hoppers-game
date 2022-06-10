package puzzles.hoppers.model;
/*
BY Shandon Mith
 */
public class HoppersClientData {
    private String message;

    public HoppersClientData(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        if (message.startsWith("Loaded")) {
            return "Loaded: " + message.substring(21);
        }
        return message;
    }

}
