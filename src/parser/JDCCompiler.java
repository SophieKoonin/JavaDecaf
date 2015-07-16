package parser;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;

/**
 * The main compiler class. Runs the precompiler on an input file then calls the system java compiler on it.
 * Some code modified from JavaCC original parser code
 * @author Sophie Koonin
 */
public class JDCCompiler {
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); //get the local system java compiler

    public static void main(String args[]) throws Exception {
        String filename;

        if (args.length == 1) {
            precompile(args[0]);  //precompile the file
        } else {
            System.out.println("Usage: \"javadecaf filename\"");
        }
    }

    /**
     * Call the JavaCC parser on the file from args[0] to convert the JavaDecaf code to true Java.
     * @param inputFile the name of the file to be used as input
     * @return the filename of the converted java file, null if something goes wrong
     */
    public static String precompile(String inputFile) {
        JDCParser parser;
        ASTCompilationUnit node;
        String className = "";
            try {
                if (Character.isDigit(inputFile.charAt(0))) { throw new ParseException("Class names in Java cannot begin with a digit."+
                        "Please rename the file.");}
                parser = new JDCParser(new FileInputStream(inputFile));
                int index = inputFile.indexOf("."); //get the index of the full stop for substring
                className = inputFile.substring(0, index); //get the name of the class from the filename (before extension)
                node = parser.CompilationUnit();
                PrintWriter ostr = new PrintWriter(new FileWriter(className+".java"));
                node.process(ostr, className);
                ostr.close();
                System.out.println("OK");
                return className + ".java"; //return the finished filename to signal successful compilation
            } catch (ParseException e) {
                System.out.println("Errors during compilation: ");
                System.out.println(e.getMessage());
            } catch (FileNotFoundException e) {
                    System.out.println("File " + inputFile + " not found.");
            } catch (IOException e) {
                System.out.println("Error creating file " + className + ".java");
            } catch (TokenMgrError e) {
                System.out.println(e.getMessage());
                if (e.errorCode != TokenMgrError.LEXICAL_ERROR) e.printStackTrace(); //only print stack trace if error is not lexical (i.e. problem with compiler)
            }
        return null;
    }

}

