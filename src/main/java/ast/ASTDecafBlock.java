package main.java.ast;

/* Modified from Transformer example provided in package for JavaCC
 *
 */
/* Copyright (c) 2006, Sun Microsystems, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Sun Microsystems, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Original code from JavaCC (see above copyright notice).
 * Modified by Sophie Koonin 2015.
 */

/* JJT: 0.2.2 */

import main.java.parser.JDCParser;
import main.java.parser.JDCParserConstants;
import main.java.parser.Token;

import java.io.*;

public class ASTDecafBlock extends ClosingBraceSimpleNode {

  ASTDecafBlock(int id) {
    super(id);
  }

    public ASTDecafBlock(JDCParser p, int id) {
        super(p, id);
    }
    /**
     * Encapsulate a "loose" block of Decaf code and perform any requisite substitutions.
     * @param ostr - output stream writer defined in JDCParser
     */
  public void process (PrintWriter ostr) {
      String classDec = "import java.util.Scanner;\n" +  //Assign the class/main method encapsulation code
              "public class " + parser.getClassName() + " { \n    " +
              "private Scanner input = new Scanner(System.in);\n    " + //init Scanner for reading from stdin
              "    public static void main(String[] args){\n    ";
      ostr.print(classDec);
//
      super.process(ostr);
  }

}
