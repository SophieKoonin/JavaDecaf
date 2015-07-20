package main.java.parser;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * The main compiler class. Runs the precompiler on an input file then calls the system java compiler on it.
 * Some code modified from JavaCC original parser code
 * @author Sophie Koonin
 */
public class JavaDecafCompiler {
    private static final double VERSION = 1.0;

    public static void main(String[] args) throws Exception {
        JavaDecafCompiler decaf = new JavaDecafCompiler();

        // Check that there are enough args
        if (args.length > 0) {
            decaf.launch(args);
        } else {
            System.out.println("usage: \"javadecaf [options] filename\"" +
            "\nType \"javadecaf -help\" to view options");
        }
    }

    /**
     * Launcher class - checks command line parameters and calls parsing and
     * compiling methods
     * @param args  - the command line arguments
     */
    public void launch(String[] args){
        Boolean parseOnly = false; //indicates Parse Only mode (no Java Compiler)

        /* Command line parameters */
        List<String> argsList = Arrays.asList(args);
        if (argsList.contains("-v") || argsList.contains("-version")) {
            System.out.println("JavaDecaf Compiler version " + VERSION);
        }
        if (argsList.contains("-p") || argsList.contains("-parse")) {
            parseOnly = true;
            System.out.println("Parse only mode enabled");
        }
        if (argsList.contains("-help")) {
            printUsage();
            System.exit(0); //stop any further execution as there may not be a filename given
        }

        long startTime = System.nanoTime();
        String inputFile = args[args.length-1]; //last item in args is the filename
        String precompiledClass = precompile(inputFile);
        long endTime = System.nanoTime();
        if (precompiledClass != null) {
            if (parseOnly) { //print success message and finish
                System.out.println("Parse completed successfully in " + ((endTime - startTime) / 1000000) + " ms");
            } else {
                compileJava(precompiledClass); //call the java compiler
                System.out.println("JavaDecaf: Compilation finished in " + ((endTime - startTime) / 1000000) + " ms"); //only print if successfully compiled
            }
        }

    }

    /**
     * Call the JavaCC parser on the file from args[0] to convert the JavaDecaf code to true Java.
     * Use the name of the JDC file for the name of the Java class, and check its validity as a Java classname.
     * @param filePath the path of the file to be used as input, to become Java class name
     * @return the filename of the converted java file, null if something goes wrong
     */
    public String precompile(String filePath) {
        JDCParser parser;
        ASTCompilationUnit node;
        String className = "";
        File inputFile;
            try {
                inputFile = new File(filePath);
                if (Character.isDigit(inputFile.getName().charAt(0))) { //Check that first char of file name is not digit
                    throw new ParseException("Class names in Java cannot begin with a digit. "+
                            "Please rename the file.");
                } else if (Character.isLowerCase(inputFile.getName().charAt(0))) { //Check that first char is uppercase
                    throw new ParseException("Class names in Java must begin with a capital letter. " +
                            "Please rename the file.");
                }
                int index = inputFile.getName().indexOf("."); //get the index of the full stop for substring
                className = inputFile.getName().substring(0, index); //get the name of the class from the filename (before extension)
                parser = new JDCParser(new FileInputStream(inputFile));
                node = parser.CompilationUnit();
                PrintWriter ostr = new PrintWriter(new FileWriter(className+".java"));
                node.process(ostr, className);
                ostr.close();
                return className + ".java"; //return the finished filename to signal successful compilation
            } catch (ParseException e) {
                System.out.println("Errors during compilation: ");
                System.out.println(e.getMessage());
            } catch (FileNotFoundException e) {
                    System.out.println("File " + filePath + " not found.");
            } catch (IOException e) {
                System.out.println("Error creating file " + className + ".java");
            } catch (TokenMgrError e) {
                System.out.println(e.getMessage());
                if (e.errorCode != TokenMgrError.LEXICAL_ERROR) e.printStackTrace(); //only print stack trace if error is not lexical (i.e. problem with compiler)
            }
        return null;
    }

    /**
     * Run the system Java compiler on a java class.
     * @param filename the name of the file to compile
     */
    public void compileJava(String filename) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); //get the local system java compiler
        if (compiler == null) {
            System.setProperty("java.home",System.getenv("JAVA_HOME")); //this should work on BBK lab computers
            compiler = ToolProvider.getSystemJavaCompiler(); //set it again
        }
        StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null); //get file manager
        Iterable<? extends JavaFileObject> fileToCompile = fileMgr.getJavaFileObjects(filename); //init file to compile from filename
        List<String> argOptions = Arrays.asList("-cp", "."); //command line options - set classpath to current working directory
        JavaCompiler.CompilationTask compTask = compiler.getTask(null, fileMgr, null, null, null, fileToCompile); //init compilation task with file mgr and file to compile
        compTask.call(); //compile the file
    }

    /**
     * Print the command line usage
     */
    public void printUsage(){
        String usage = "usage: javadecaf [options] filename" +
                "\noptions:"+
                "\n-p | -parse      Parse-only mode - disable Java compiler stage" +
                "\n-v | -version    Display version number" +
                "\n-help            Show help";

        System.out.println(usage);
    }

}
