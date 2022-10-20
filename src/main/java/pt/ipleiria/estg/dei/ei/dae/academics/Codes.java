package pt.ipleiria.estg.dei.ei.dae.academics;

public abstract class Codes {
    private Codes() { }  // Prevents instantiation

    public static final int OK = 0;
    public static final int ERROR = 1;

    public static final int USER_NOT_FOUND = 2;
    public static final int SUBJECT_NOT_FOUND = 3;
    public static final int SUBJECT_COURSE_NOT_MATCH = 4;
    public static final int USER_ALREADY_ENROLLED = 5;
    public static final int USER_NOT_ENROLLED = 6;

}