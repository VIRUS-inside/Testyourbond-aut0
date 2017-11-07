package org.apache.regexp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;






























































public class RETest
{
  RE r = new RE();
  REDebugCompiler compiler = new REDebugCompiler();
  




  static final boolean showSuccesses = false;
  




  public static void main(String[] paramArrayOfString)
  {
    try
    {
      test();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  




  public static boolean test()
    throws Exception
  {
    RETest localRETest = new RETest();
    localRETest.runAutomatedTests("docs/RETest.txt");
    return failures == 0;
  }
  












  public RETest(String[] paramArrayOfString)
  {
    try
    {
      if (paramArrayOfString.length == 2)
      {
        runInteractiveTests(paramArrayOfString[1]);
      }
      else if (paramArrayOfString.length == 1)
      {

        runAutomatedTests(paramArrayOfString[0]);
      }
      else
      {
        System.out.println("Usage: RETest ([-i] [regex]) ([/path/to/testfile.txt])");
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  





  void runInteractiveTests(String paramString)
  {
    try
    {
      r.setProgram(compiler.compile(paramString));
      

      say("\n" + paramString + "\n");
      

      compiler.dumpProgram(new PrintWriter(System.out));
      


      for (;;)
      {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("> ");
        System.out.flush();
        String str = localBufferedReader.readLine();
        

        if (r.match(str))
        {
          say("Match successful.");
        }
        else
        {
          say("Match failed.");
        }
        

        showParens(r);
      }
    }
    catch (Exception localException)
    {
      say("Error: " + localException.toString());
      localException.printStackTrace();
    }
  }
  




  void die(String paramString)
  {
    say("FATAL ERROR: " + paramString);
    System.exit(0);
  }
  




  void fail(String paramString)
  {
    failures += 1;
    say("\n");
    say("*******************************************************");
    say("*********************  FAILURE!  **********************");
    say("*******************************************************");
    say("\n");
    say(paramString);
    say("");
    compiler.dumpProgram(new PrintWriter(System.out));
    say("\n");
  }
  

















  void say(String paramString)
  {
    System.out.println(paramString);
  }
  



  void show()
  {
    say("\n-----------------------\n");
    say("Expression #" + n + " \"" + expr + "\" ");
  }
  





  void showParens(RE paramRE)
  {
    for (int i = 0; i < paramRE.getParenCount(); i++)
    {

      say("$" + i + " = " + paramRE.getParen(i));
    }
  }
  


  char[] re1Instructions = {
    '|', '\000', '\032', '|', '\000', '\r', 'A', 
    '\001', '\004', 'a', '|', '\000', '\003', 'G', '\000', 
    65526, '|', '\000', '\003', 'N', '\000', 
    '\003', 'A', '\001', '\004', 'b', 'E' };
  


  REProgram re1 = new REProgram(re1Instructions);
  

  String expr;
  

  int n = 0;
  



  int failures = 0;
  



  void runAutomatedTests(String paramString)
    throws Exception
  {
    long l = System.currentTimeMillis();
    

    RE localRE = new RE(re1);
    say("a*b");
    say("aaaab = " + localRE.match("aaab"));
    showParens(localRE);
    say("b = " + localRE.match("b"));
    showParens(localRE);
    say("c = " + localRE.match("c"));
    showParens(localRE);
    say("ccccaaaaab = " + localRE.match("ccccaaaaab"));
    showParens(localRE);
    
    localRE = new RE("a*b");
    String[] arrayOfString = localRE.split("xxxxaabxxxxbyyyyaaabzzz");
    localRE = new RE("x+");
    arrayOfString = localRE.grep(arrayOfString);
    for (int i = 0; i < arrayOfString.length; i++)
    {
      System.out.println("s[" + i + "] = " + arrayOfString[i]);
    }
    
    localRE = new RE("a*b");
    String str1 = localRE.subst("aaaabfooaaabgarplyaaabwackyb", "-");
    System.out.println("s = " + str1);
    

    File localFile = new File(paramString);
    if (!localFile.exists())
      throw new Exception("Could not find: " + paramString);
    BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
    
    try
    {
      while (localBufferedReader.ready())
      {

        String str2 = "";
        
        while (localBufferedReader.ready())
        {
          str2 = localBufferedReader.readLine();
          if (str2 == null) {
            break;
          }
          
          str2 = str2.trim();
          if (str2.startsWith("#")) {
            break;
          }
          
          if (!str2.equals(""))
          {
            System.out.println("Script error.  Line = " + str2);
            System.exit(0);
          }
        }
        

        if (!localBufferedReader.ready()) {
          break;
        }
        


        expr = localBufferedReader.readLine();
        n += 1;
        say("");
        say(n + ". " + expr);
        say("");
        
        String str3;
        try
        {
          localRE.setProgram(compiler.compile(expr));


        }
        catch (Exception localException1)
        {

          str3 = localBufferedReader.readLine().trim();
          

          if (str3.equals("ERR"))
          {
            say("   Match: ERR");
            success("Produces an error (" + localException1.toString() + "), as expected.");
            continue;
          }
          

          fail("Produces the unexpected error \"" + localException1.getMessage() + "\"");

        }
        catch (Error localError1)
        {
          fail("Compiler threw fatal error \"" + localError1.getMessage() + "\"");
          localError1.printStackTrace();
        }
        

        String str4 = localBufferedReader.readLine().trim();
        say("   Match against: '" + str4 + "'");
        

        if (str4.equals("ERR"))
        {
          fail("Was expected to be an error, but wasn't.");

        }
        else
        {
          try
          {

            boolean bool = localRE.match(str4);
            

            str3 = localBufferedReader.readLine().trim();
            

            if (bool)
            {

              say("   Match: YES");
              

              if (str3.equals("NO"))
              {
                fail("Matched \"" + str4 + "\", when not expected to.");

              }
              else if (str3.equals("YES"))
              {

                success("Matched \"" + str4 + "\", as expected:");
                






                say("   Paren count: " + localRE.getParenCount());
                

                for (int j = 0; j < localRE.getParenCount(); j++)
                {

                  String str5 = localBufferedReader.readLine().trim();
                  say("   Paren " + j + " : " + localRE.getParen(j));
                  

                  if (!str5.equals(localRE.getParen(j)))
                  {

                    fail("Register " + j + " should be = \"" + str5 + "\", but is \"" + localRE.getParen(j) + "\" instead.");
                  }
                  
                }
              }
              else
              {
                die("Test script error!");
              }
              
            }
            else
            {
              say("   Match: NO");
              

              if (str3.equals("YES"))
              {

                fail("Did not match \"" + str4 + "\", when expected to.");

              }
              else if (str3.equals("NO"))
              {

                success("Did not match \"" + str4 + "\", as expected.");

              }
              else
              {
                die("Test script error!");
              }
              
            }
            
          }
          catch (Exception localException2)
          {
            fail("Matcher threw exception: " + localException2.toString());
            localException2.printStackTrace();

          }
          catch (Error localError2)
          {

            fail("Matcher threw fatal error \"" + localError2.getMessage() + "\"");
            localError2.printStackTrace();
          }
        }
      }
    }
    finally {
      localBufferedReader.close();
    }
    

    System.out.println("\n\nMatch time = " + (System.currentTimeMillis() - l) + " ms.");
    

    System.out.println("\nTests complete.  " + n + " tests, " + failures + " failure(s).");
  }
  
  public RETest() {}
  
  void success(String paramString) {}
}
