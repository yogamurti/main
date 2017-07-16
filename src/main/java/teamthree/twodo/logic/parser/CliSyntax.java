package teamthree.twodo.logic.parser;

// Contains Command Line Interface (CLI) syntax definitions common to multiple commands
public class CliSyntax {

    // Prefix definitions
    public static final Prefix PREFIX_NAME = new Prefix("n/"); //m for main
    public static final Prefix PREFIX_DEADLINE_START = new Prefix("s/");
    public static final Prefix PREFIX_DEADLINE_END = new Prefix("e/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_NOTIFICATION_PERIOD = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_AUTOMARK = new Prefix("m/");

}
