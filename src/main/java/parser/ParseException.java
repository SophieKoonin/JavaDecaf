/* Generated By:JavaCC: Do not edit this line. ParseException.java Version 5.0 */
/* JavaCCOptions:KEEP_LINE_COL=null */
package main.java.parser;

/**
 * This exception is thrown when parse errors are encountered.
 * You can explicitly create objects of this exception type by
 * calling the method generateParseException in the generated
 * parser.
 *
 * You can modify this class to customize your error reporting
 * mechanisms so long as you retain the public fields.
 */
public class ParseException extends Exception {

  /**
   * The version identifier for this Serializable class.
   * Increment only if the <i>serialized</i> form of the
   * class changes.
   */
  private static final long serialVersionUID = 1L;

  /**
   * This constructor is used by the method "generateParseException"
   * in the generated parser.  Calling this constructor generates
   * a new object of this type with the fields "currentToken",
   * "expectedTokenSequences", and "tokenImage" set.
   */
  public ParseException(Token currentTokenVal,
                        int[][] expectedTokenSequencesVal,
                        String[] tokenImageVal
                       )
  {
    super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
    currentToken = currentTokenVal;
    expectedTokenSequences = expectedTokenSequencesVal;
    tokenImage = tokenImageVal;
  }

  /**
   * The following constructors are for use by you for whatever
   * purpose you can think of.  Constructing the exception in this
   * manner makes the exception behave in the normal way - i.e., as
   * documented in the class "Throwable".  The fields "errorToken",
   * "expectedTokenSequences", and "tokenImage" do not contain
   * relevant information.  The JavaCC generated code does not use
   * these constructors.
   */

  public ParseException() {
    super();
  }

  /** Constructor with message. */
  public ParseException(String message) {
    super(message);
  }

  /**
   * This is the last token that has been consumed successfully.  If
   * this object has been created due to a parse error, the token
   * followng this token will (therefore) be the first error token.
   */
  public Token currentToken;

  /**
   * Each entry in this array is an array of integers.  Each array
   * of integers represents a sequence of tokens (by their ordinal
   * values) that is expected at this point of the parse.
   */
  public int[][] expectedTokenSequences;

  /**
   * This is a reference to the "tokenImage" array of the generated
   * parser within which the parse error occurred.  This array is
   * defined in the generated ...Constants interface.
   */
  public String[] tokenImage;

    /* Begin modified code by SK */

  /**
   * It uses "currentToken" and "expectedTokenSequences" to generate a parse
   * error message and returns it.  If this object has been created
   * due to a parse error, and you do not catch it (it gets thrown
   * from the parser) the correct error message
   * gets displayed.
   */
  private static String initialise(Token currentToken,
                           int[][] expectedTokenSequences,
                           String[] tokenImage) {
    String eol = System.getProperty("line.separator", "\n");

    String retval = "\nEncountered \"";
      if (currentToken.image == null) {   //if current token is null, probably caused by lookahead, move to next token (avoid NullPointerException) - SK
          currentToken = currentToken.next;
      }
    Token nextToken = currentToken.next;
    for (int i = 0; i < expectedTokenSequences[0].length; i++) {
      if (i != 0) retval += " ";

      retval += currentToken.image; //print the token appearing before the error
      retval += "\" followed by \"";
        if (nextToken.kind == 0) {  //end of file
            retval += "end of file";
            break;
        }
      retval += add_escapes(nextToken.image); //print the offending token
    }
    retval += "\" at line " + currentToken.endLine + ", column " + currentToken.endColumn;
    retval += "." + eol;

      /* check reason for error - SK */

      retval += getReasonForError(currentToken, nextToken, expectedTokenSequences);

    return retval;
  }

    /**
     * Check the reason for the error
     * @param currentToken the current token
     * @param nextToken the (offending) next token
     * @param expectedTokenSequences the list of expected tokens
     * @return the warning message as a String
     */
    private static String getReasonForError(Token currentToken, Token nextToken, int[][] expectedTokenSequences){ //SK
        String retval = "";
            /* current token is IDENTIFIER and next is any literal - likely missing parenthesis of argument */
        if (isIdentifier(currentToken.kind) && isLiteral(nextToken.kind)) {
            retval += "You may have forgotten to include parentheses around an argument- \"" + currentToken.image + "(" + currentToken.next.image + ")\"";

      /* current token is SEMICOLON or RBRACE and next is EOF - missing RBRACE */
        } else if ((currentToken.kind == JDCParserConstants.SEMICOLON || currentToken.kind == JDCParserConstants.RBRACE) && nextToken.kind == JDCParserConstants.EOF) {
            retval += "You may have forgotten a closing brace } after \"" + currentToken.image + "\"";

      /* current token is RPAREN, IDENTIFIER or any literal, and next token is EOF or a newline - missing semicolon at end of line*/
        } else if ((currentToken.kind == JDCParserConstants.RPAREN || isIdentifier(currentToken.kind) || isLiteral(currentToken.kind)) &&
                (nextToken.kind == JDCParserConstants.EOF ||
                        expectedTokenSequences[0][0] == JDCParserConstants.SEMICOLON)) {
            retval += "You may be missing a semicolon after \"" + currentToken.image + "\"";

          /* current token is STRING_LITERAL and next token is IDENTIFIER - bad concatenation or no escaping of special chars */
        } else if (currentToken.kind == JDCParserConstants.STRING_LITERAL && isIdentifier(nextToken.kind)) {
            retval += "If you are using quotation marks within a string, you need to escape them by adding a backslash, e.g.: \nprintln(\"say \\\"hello\\\"!\") ." +
                    "\nIf you are trying to join multiple strings, make sure you concatenate them with + , e.g.: \nprintln(\"hello\" + name + \"!\")\"; .";

        /* current token is IDENTIFIER,  next is IDENTIFIER or any literal, and parser expects a RPAREN - this expected token is common to all test cases
         * missing quotation mark, not separating method arguments with a comma, or bad concatenation
         */
        } else if (isIdentifier(currentToken.kind) && (isIdentifier(nextToken.kind) || isLiteral(nextToken.kind)) && expectedTokenSequences[1][0] == JDCParserConstants.RPAREN) {
            retval += "You may be missing a quotation mark." +
                    "\nIf you are trying to join multiple strings, make sure you concatenate them with + , e.g.: \nprintln(greeting + name + \"!\")\"; ." +
                    "\nIf these variables are separate method arguments, they should be separated by commas, e.g.: \nmethod(arg1, arg 2);";

      /*  Current token is ASSIGN and next token is GT or LT - <= or >= in incorrect order */
        } else if (currentToken.kind == JDCParserConstants.ASSIGN && (nextToken.kind == JDCParserConstants.GT || nextToken.kind == JDCParserConstants.LT)) {
            retval += "Did you mean: " + nextToken.image + currentToken.image;

          /* Current token is RPAREN, literal or IDENTIFIER and next token is LBRACE or SEMICOLON, and expected token is RPAREN
           * Missing right parenthesis */
        } else if ((currentToken.kind == JDCParserConstants.RPAREN || isLiteral(currentToken.kind) || isIdentifier(currentToken.kind))
                && (nextToken.kind == JDCParserConstants.LBRACE || nextToken.kind == JDCParserConstants.SEMICOLON)
                && (expectedTokenSequences.length>1 && expectedTokenSequences[1][0] == JDCParserConstants.RPAREN)) { //check length of expectedTokenSequences to avoid loop with semicolon being caught here
            retval += "You may be missing a closing parenthesis after \"" + currentToken.image + "\".";

          /* Current token is RPAREN and next is SEMICOLON, and first expected token is 'throws' - method declaration with semicolon */
        } else if (currentToken.kind == JDCParserConstants.RPAREN && nextToken.kind == JDCParserConstants.SEMICOLON && expectedTokenSequences[0][0] == JDCParserConstants.THROWS) { //rparen, semicolon, and first expected token is 'throws'
            retval += "You do not need a semicolon in the first line of a method declaration, e.g.: \nvoid greet(String name) { \n    println(\"Hello\" + name); \n}";

      /* Current token is identifier, primitive or "void" followed by a reserved keyword - reserved keyword used as variable or method name */
        } else if ((isIdentifier(currentToken.kind)|| currentToken.image.equals("void") || isPrimitive(currentToken.kind)) && //identifier or "void" followed by reserved keyword
                isReservedKeyword(nextToken.kind)) {
            retval += "You cannot use \"" + nextToken.image + "\" as a method or variable name in Java because it is a reserved keyword.";

      /* Next token is if, for or while and the following token is NOT a RPAREN - missing parentheses around loop condition.
      * Lookahead will spot this error, hence currentToken will be the one before the loop keyword. */
        } else if ((nextToken.kind == JDCParserConstants.IF || nextToken.kind == JDCParserConstants.FOR || nextToken.kind == JDCParserConstants.WHILE)
                && nextToken.next.kind != JDCParserConstants.RPAREN) {
            retval += "You may have forgotten parentheses around the loop condition."+
                    "\n\'if\', \'for\' and \'while\' loop conditions should be in parentheses () and the loop body should be in curly braces { }, e.g.: \n" +
                    "if (x > y) {\n" +
                    "    println(x);\n" +
                    "}";

          /* Current token is IF, FOR or WHILE, and next token is NOT RPAREN - this is the same as previous error but without lookahead */
        } else if ((currentToken.kind == JDCParserConstants.IF || currentToken.kind == JDCParserConstants.FOR || currentToken.kind == JDCParserConstants.WHILE)
                && nextToken.kind != JDCParserConstants.RPAREN) { //error with loop, no lookahead - currentToken is loop keyword
            retval += "You may have forgotten parentheses around the loop condition."+
                    "\n\'if\', \'for\' and \'while\' loop conditions should be in parentheses () and the loop body should be in curly braces { }, e.g.: \n" +
                    "if (x > y) {\n" +
                    "    println(x);\n" +
                    "}";

          /* Current token is IDENTIFIER or primitive and next token is ASSIGN - no variable name declared */
        } else if ((isIdentifier(currentToken.kind) || isPrimitive(currentToken.kind)) && nextToken.kind == JDCParserConstants.ASSIGN) {
            retval += "You may have forgotten to define a name for a variable, e.g.: int myNum = 5; ";

          /* Next token is RBRACKET or RBRACE, when RPAREN expected - wrong kind of bracket used*/
        } else if ((nextToken.kind == JDCParserConstants.RBRACKET || nextToken.kind == JDCParserConstants.RBRACE) && (expectedTokenSequences[1][0] == JDCParserConstants.RPAREN || expectedTokenSequences[0][0] == JDCParserConstants.RPAREN)) {
            retval += "You may have used a brace { } or square bracket [ ] instead of a parenthesis ( ).";

      /* Current token is RPAREN and next is SEMICOLON, and LBRACE expected - semicolon inserted after loop condition */
        } else if (currentToken.kind == JDCParserConstants.RPAREN && nextToken.kind == JDCParserConstants.SEMICOLON && expectedTokenSequences[0][0] == JDCParserConstants.LBRACE) {
            retval += "You may have inserted a semicolon after a loop condition. This will cause the statement to be evaluated incorrectly.";

          /* Current token is RPAREN and next is NOT a LBRACE when one is expected - loop/branch statement without curly braces */
        } else if (currentToken.kind == JDCParserConstants.RPAREN && nextToken.kind != JDCParserConstants.LBRACE && expectedTokenSequences[0][0] == JDCParserConstants.LBRACE) {
            retval += "Loop statements must be contained in curly braces, e.g.:\n " +
                    "if (x > y) {\n" +
                    "    println(x);\n" +
                    "}";

        } else if ((isIdentifier(currentToken.kind) || isPrimitive(currentToken.kind))  && nextToken.kind == JDCParserConstants.DIGIT){
            retval += "Method and variable names should not begin with a number or be entirely numeric, e.g.:\n" +
                    "Acceptable: void method1(), int num1\n" +
                    "Not acceptable: void 1(), int 1st";
        }
        return retval;
    }

    /**
     * Check to see if a token is a reserved keyword
     * @return true if token is a reserved keyword, otherwise false
     */
    public static boolean isReservedKeyword(int kind){  //SK
        return (kind >= JDCParserConstants.ABSTRACT && kind <= JDCParserConstants.WHILE);
    }

    /**
     * Check to see if token is an identifier
     * @return true if identifier, otherwise false
     */
    public static boolean isIdentifier(int kind) { //SK
        return (kind == JDCParserConstants.IDENTIFIER);
    }

    /**
     * Check to see if a token is a literal
     * @return true if literal, otherwise false
     */
    public static boolean isLiteral(int kind){ //SK
        return (kind >= JDCParserConstants.INTEGER_LITERAL && kind <= JDCParserConstants.STRING_LITERAL);
    }


    /**
     * Check to see if a token is a primitive type
     * @return true if primitive type, otherwise false
     */
    public static boolean isPrimitive(int kind){ //SK
        return (kind == JDCParserConstants.INT
                || kind == JDCParserConstants.FLOAT
                || kind == JDCParserConstants.SHORT
                || kind == JDCParserConstants.BYTE
                || kind == JDCParserConstants.DOUBLE
                || kind == JDCParserConstants.CHAR
                || kind == JDCParserConstants.BOOLEAN
                || kind == JDCParserConstants.LONG);
    }
    /* End of modified code by SK */

  /**
   * The end of line string for this machine.
   */
  protected static final String EOL = System.getProperty("line.separator", "\n");

  /**
   * Used to convert raw characters to their escaped version
   * when these raw version cannot be used as part of an ASCII
   * string literal.
   */
  static String add_escapes(String str) {
      StringBuffer retval = new StringBuffer();
      char ch;
      for (int i = 0; i < str.length(); i++) {
        switch (str.charAt(i))
        {
           case 0 :
              continue;
           case '\b':
              retval.append("\\b");
              continue;
           case '\t':
              retval.append("\\t");
              continue;
           case '\n':
              retval.append("\\n");
              continue;
           case '\f':
              retval.append("\\f");
              continue;
           case '\r':
              retval.append("\\r");
              continue;
           case '\"':;
              retval.append("\\\"");
              continue;
           case '\'':
              retval.append("\\\'");
              continue;
           case '\\':
              retval.append("\\\\");
              continue;
           default:
              if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
                 String s = "0000" + Integer.toString(ch, 16);
                 retval.append("\\u" + s.substring(s.length() - 4, s.length()));
              } else {
                 retval.append(ch);
              }
              continue;
        }
      }
      return retval.toString();
   }

}
/* JavaCC - OriginalChecksum=16ba3edbb338127335444b2b77a37aa1 (do not edit this line) */
