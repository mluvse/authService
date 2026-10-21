package by.mluvse.authService;

public enum Role {

    ADMIN("ADMIN"),
    USER("USER");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
