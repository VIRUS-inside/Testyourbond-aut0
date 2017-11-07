package org.apache.regexp;

import java.io.PrintWriter;
import java.util.Hashtable;



































































public class REDebugCompiler
  extends RECompiler
{
  static Hashtable hashOpcode = new Hashtable();
  
  static {
    hashOpcode.put(new Integer(56), "OP_RELUCTANTSTAR");
    hashOpcode.put(new Integer(61), "OP_RELUCTANTPLUS");
    hashOpcode.put(new Integer(47), "OP_RELUCTANTMAYBE");
    hashOpcode.put(new Integer(69), "OP_END");
    hashOpcode.put(new Integer(94), "OP_BOL");
    hashOpcode.put(new Integer(36), "OP_EOL");
    hashOpcode.put(new Integer(46), "OP_ANY");
    hashOpcode.put(new Integer(91), "OP_ANYOF");
    hashOpcode.put(new Integer(124), "OP_BRANCH");
    hashOpcode.put(new Integer(65), "OP_ATOM");
    hashOpcode.put(new Integer(42), "OP_STAR");
    hashOpcode.put(new Integer(43), "OP_PLUS");
    hashOpcode.put(new Integer(63), "OP_MAYBE");
    hashOpcode.put(new Integer(78), "OP_NOTHING");
    hashOpcode.put(new Integer(71), "OP_GOTO");
    hashOpcode.put(new Integer(92), "OP_ESCAPE");
    hashOpcode.put(new Integer(40), "OP_OPEN");
    hashOpcode.put(new Integer(41), "OP_CLOSE");
    hashOpcode.put(new Integer(35), "OP_BACKREF");
    hashOpcode.put(new Integer(80), "OP_POSIXCLASS");
  }
  






  String opcodeToString(char paramChar)
  {
    String str = (String)hashOpcode.get(new Integer(paramChar));
    

    if (str == null)
    {
      str = "OP_????";
    }
    return str;
  }
  






  String charToString(char paramChar)
  {
    if ((paramChar < ' ') || (paramChar > ''))
    {
      return "\\" + paramChar;
    }
    

    return String.valueOf(paramChar);
  }
  






  String nodeToString(int paramInt)
  {
    char c = instruction[paramInt];
    int i = instruction[(paramInt + 1)];
    

    return opcodeToString(c) + ", opdata = " + i;
  }
  





  public void dumpProgram(PrintWriter paramPrintWriter)
  {
    for (int i = 0; i < lenInstruction;)
    {

      int j = instruction[i];
      int k = instruction[(i + 1)];
      int m = (short)instruction[(i + 2)];
      

      paramPrintWriter.print(i + ". " + nodeToString(i) + ", next = ");
      

      if (m == 0)
      {
        paramPrintWriter.print("none");
      }
      else
      {
        paramPrintWriter.print(i + m);
      }
      

      i += 3;
      
      int n;
      if (j == 91)
      {

        paramPrintWriter.print(", [");
        

        n = k;
        for (int i1 = 0; i1 < n; i1++)
        {

          char c1 = instruction[(i++)];
          char c2 = instruction[(i++)];
          

          if (c1 == c2)
          {
            paramPrintWriter.print(charToString(c1));
          }
          else
          {
            paramPrintWriter.print(charToString(c1) + "-" + charToString(c2));
          }
        }
        

        paramPrintWriter.print("]");
      }
      

      if (j == 65)
      {

        paramPrintWriter.print(", \"");
        

        for (n = k; n-- != 0;)
        {
          paramPrintWriter.print(charToString(instruction[(i++)]));
        }
        

        paramPrintWriter.print("\"");
      }
      

      paramPrintWriter.println("");
    }
  }
  
  public REDebugCompiler() {}
}
