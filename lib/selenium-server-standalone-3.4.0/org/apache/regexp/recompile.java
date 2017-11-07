package org.apache.regexp;

import java.io.PrintStream;



































































































public class recompile
{
  public recompile() {}
  
  public static void main(String[] paramArrayOfString)
  {
    RECompiler localRECompiler = new RECompiler();
    

    if ((paramArrayOfString.length <= 0) || (paramArrayOfString.length % 2 != 0))
    {
      System.out.println("Usage: recompile <patternname> <pattern>");
      System.exit(0);
    }
    

    for (int i = 0; i < paramArrayOfString.length; i += 2)
    {
      try
      {

        String str1 = paramArrayOfString[i];
        String str2 = paramArrayOfString[(i + 1)];
        String str3 = str1 + "PatternInstructions";
        

        System.out.print("\n    // Pre-compiled regular expression '" + str2 + "'\n" + 
          "    private static char[] " + str3 + " = \n    {");
        

        REProgram localREProgram = localRECompiler.compile(str2);
        

        int j = 7;
        

        char[] arrayOfChar = localREProgram.getInstructions();
        for (int k = 0; k < arrayOfChar.length; k++)
        {

          if (k % j == 0)
          {
            System.out.print("\n        ");
          }
          

          String str4 = Integer.toHexString(arrayOfChar[k]);
          while (str4.length() < 4)
          {
            str4 = "0" + str4;
          }
          System.out.print("0x" + str4 + ", ");
        }
        

        System.out.println("\n    };");
        System.out.println("\n    private static RE " + str1 + "Pattern = new RE(new REProgram(" + str3 + "));");
      }
      catch (RESyntaxException localRESyntaxException)
      {
        System.out.println("Syntax error in expression \"" + paramArrayOfString[i] + "\": " + localRESyntaxException.toString());
      }
      catch (Exception localException)
      {
        System.out.println("Unexpected exception: " + localException.toString());
      }
      catch (Error localError)
      {
        System.out.println("Internal error: " + localError.toString());
      }
    }
  }
}
