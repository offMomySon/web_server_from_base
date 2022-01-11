package thread;

public enum ThreadTaskType {
    MAIN, THREAD;

    public boolean isMain() {
        return this == MAIN;
    }
}
