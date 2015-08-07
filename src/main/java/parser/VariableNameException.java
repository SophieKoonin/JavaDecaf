package main.java.parser;

/**
 * Exception thrown when variable name is not acceptable according to Java convention.
 */
public class VariableNameException extends ParseException {
    protected static final String EOL = System.getProperty("line.separator", "\n");

    public VariableNameException(Token t){
        super(initMessage(t));
    }

    /**
     * Initialise the message for this exception with the name of the class
     * @param t the token of the classname
     * @return the error message
     */
    public static String initMessage(Token t) {
        String message = "Error: encountered variable name " + t.image + " at line " + t.beginLine + ", column "+ t.beginColumn +
                ". Variable names should begin with a lower case letter, with all subsequent words in CamelCase. They should also be nouns. e.g.:" + EOL +
                "int myNum" + EOL + "int heightOfTallestBuilding" + EOL + "String name" + EOL + "Person person1" + EOL + "int maxSoFar";
        return message;

    }

}