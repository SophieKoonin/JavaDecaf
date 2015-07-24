/* Generated By:JJTree: Do not edit this line. ASTDecafClass.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package main.java.ast;

import main.java.parser.*;

import java.io.PrintWriter;

public
class ASTDecafClass extends ClosingBraceSimpleNode {
  public ASTDecafClass(int id) {
    super(id);
  }

  public ASTDecafClass(JDCParser p, int id) {
    super(p, id);
  }

    public void process(PrintWriter ostr) {
        String classDec = "import java.util.Scanner;\n\n" +  //Assign the class/main method encapsulation code
                "public class " + parser.getClassName() + " { \n" + ASTUtils.INDENTATION +
                "private Scanner input = new Scanner(System.in);\n"  + ASTUtils.INDENTATION; //init Scanner for reading from stdin

        ostr.print(classDec);
        super.process(ostr);
    }


}
/* JavaCC - OriginalChecksum=ba592f10a30b87d2e827995b8d4e8c28 (do not edit this line) */
