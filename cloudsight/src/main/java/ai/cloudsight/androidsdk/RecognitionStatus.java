package ai.cloudsight.androidsdk;

public enum RecognitionStatus {
    COMPLETED("completed"),
    SKIPPED("skipped"),
    TIMEOUT("timeout"),
    NOT_FOUND("not found"),
    NOT_COMPLETED("not completed");

    private String status;

    RecognitionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}