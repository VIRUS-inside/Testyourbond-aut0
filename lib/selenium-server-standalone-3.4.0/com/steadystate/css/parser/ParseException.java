package com.steadystate.css.parser;











public class ParseException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  








  protected static String EOL = System.getProperty("line.separator", "\n");
  

  public Token currentToken;
  

  public int[][] expectedTokenSequences;
  
  public String[] tokenImage;
  

  public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal)
  {
    super(initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
    currentToken = currentTokenVal;
    expectedTokenSequences = expectedTokenSequencesVal;
    tokenImage = tokenImageVal;
  }
  






  public ParseException() {}
  






  public ParseException(String message)
  {
    super(message);
  }
  































  private static String initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage)
  {
    StringBuffer expected = new StringBuffer();
    int maxSize = 0;
    for (int i = 0; i < expectedTokenSequences.length; i++) {
      if (maxSize < expectedTokenSequences[i].length) {
        maxSize = expectedTokenSequences[i].length;
      }
      for (int j = 0; j < expectedTokenSequences[i].length; j++) {
        expected.append(tokenImage[expectedTokenSequences[i][j]]).append(' ');
      }
      if (expectedTokenSequences[i][(expectedTokenSequences[i].length - 1)] != 0) {
        expected.append("...");
      }
      expected.append(EOL).append("    ");
    }
    String retval = "Encountered \"";
    Token tok = next;
    for (int i = 0; i < maxSize; i++) {
      if (i != 0) retval = retval + " ";
      if (kind == 0) {
        retval = retval + tokenImage[0];
        break;
      }
      retval = retval + " " + tokenImage[kind];
      retval = retval + " \"";
      retval = retval + add_escapes(image);
      retval = retval + " \"";
      tok = next;
    }
    retval = retval + "\" at line " + next.beginLine + ", column " + next.beginColumn;
    retval = retval + "." + EOL;
    

    if (expectedTokenSequences.length != 0)
    {

      if (expectedTokenSequences.length == 1) {
        retval = retval + "Was expecting:" + EOL + "    ";
      } else {
        retval = retval + "Was expecting one of:" + EOL + "    ";
      }
      retval = retval + expected.toString();
    }
    
    return retval;
  }
  





  static String add_escapes(String str)
  {
    StringBuffer retval = new StringBuffer();
    
    for (int i = 0; i < str.length(); i++) {
      switch (str.charAt(i))
      {
      case '\b': 
        retval.append("\\b");
        break;
      case '\t': 
        retval.append("\\t");
        break;
      case '\n': 
        retval.append("\\n");
        break;
      case '\f': 
        retval.append("\\f");
        break;
      case '\r': 
        retval.append("\\r");
        break;
      case '"': 
        retval.append("\\\"");
        break;
      case '\'': 
        retval.append("\\'");
        break;
      case '\\': 
        retval.append("\\\\");
        break;
      default:  char ch;
        if (((ch = str.charAt(i)) < ' ') || (ch > '~')) {
          String s = "0000" + Integer.toString(ch, 16);
          retval.append("\\u" + s.substring(s.length() - 4, s.length()));
        } else {
          retval.append(ch);
        }
        break;
      }
    }
    return retval.toString();
  }
}
