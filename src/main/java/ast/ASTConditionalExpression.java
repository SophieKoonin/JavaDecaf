/* Generated By:JJTree: Do not edit this line. ASTConditionalExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=true,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package main.java.ast;

import main.java.parser.*;

import java.io.PrintWriter;

public
class ASTConditionalExpression extends SimpleNode {
    public ASTConditionalExpression(int id) {
        super(id);
    }

    public ASTConditionalExpression(JDCParser p, int id) {
        super(p, id);
        indent = false;
    }

    public void process(PrintWriter ostr) {
        Token t = begin;
        while (t!= end) {
            if (t.image.equals("&")) {
                String warning = "\"You have used single \\'&\\' instead of double \\'&&\\'. Logical AND in Java is represented using \\'&&\\'.\"";
                StyleWarnings.generateWarning(t, parser, warning);
            }
            if (t.image.equals("|")) {
                String warning = "You have used single \\'|\\' instead of double \\'||\\'. Logical OR in Java is represented using \\'||\\'.\"";
                StyleWarnings.generateWarning(t, parser, warning);
            }
            t = t.next;
        }
    }

}
/* JavaCC - OriginalChecksum=38ffaa9dd63c3f3e866095e4532ecd91 (do not edit this line) */
