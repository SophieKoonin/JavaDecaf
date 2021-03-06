package parser;

/**
 * Class containing static method for generating style warnings.
 * @author Sophie Koonin
 */
public class StyleWarnings {

    /**
     * Create a warning message with the token image, line and column of the token, and add it to the parser's
     * warning list.
     * @param t the offending token
     * @param parser the parser in use
     * @param message the warning message
     */
    public static void generateWarning(Token t, JDCParser parser, String message) {
        String warning = "Warning: encountered \"" + t.image + "\" at line " + t.endLine + ", column " + t.endColumn +
                ": " + message;
	  /*
	   * FIXME: disabling warnings for now. 
	   * 
	   * TODO: 
	   *   - set levels of warning on the command line
	   *   - fix the indentation warnings
	   */
        // FIXME: disabling for now  ----> parser.addWarning(warning);
    }

}
