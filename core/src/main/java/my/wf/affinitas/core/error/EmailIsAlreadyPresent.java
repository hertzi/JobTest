package my.wf.affinitas.core.error;

public class EmailIsAlreadyPresent extends RuntimeException {
    private String email;

    public String getEmail() {
        return email;
    }

    public EmailIsAlreadyPresent(String email) {
        this.email = email;
    }

    public EmailIsAlreadyPresent(String message, String email) {
        super(message);
        this.email = email;
    }

    public EmailIsAlreadyPresent(String message, Throwable cause, String email) {
        super(message, cause);
        this.email = email;
    }

    public EmailIsAlreadyPresent(Throwable cause, String email) {
        super(cause);
        this.email = email;
    }

    public EmailIsAlreadyPresent(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String email) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.email = email;
    }
}
