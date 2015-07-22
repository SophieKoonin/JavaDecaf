/* Generated By:JJTree: Do not edit this line. ASTStatementExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package main.java.parser;

import java.io.PrintWriter;

public
class ASTStatementExpression extends SimpleNode {
  public ASTStatementExpression(int id) {
    super(id);
  }

  public ASTStatementExpression(JDCParser p, int id) {
    super(p, id);
  }

  public void process(PrintWriter ostr) {
    /* if the parent node is a loop, check indentation of first token */
    if (jjtGetParent().toString().equals("IfStatement")) {
      StyleWarnings.checkIndentation(p, begin, 1);

    }
  }
}
/* JavaCC - OriginalChecksum=e873b876e7f466c17fe124d220aedcae (do not edit this line) */
