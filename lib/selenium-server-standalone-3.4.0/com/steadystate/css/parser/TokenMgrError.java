package com.steadystate.css.parser;







public class TokenMgrError
  extends Error
{
  private static final long serialVersionUID = 1L;
  




  public static final int LEXICAL_ERROR = 0;
  




  public static final int STATIC_LEXER_ERROR = 1;
  




  public static final int INVALID_LEXICAL_STATE = 2;
  




  public static final int LOOP_DETECTED = 3;
  




  int errorCode;
  





  protected static final String addEscapes(String str)
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
  











  protected static String LexicalErr(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar)
  {
    char curChar1 = (char)curChar;
    



    return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : new StringBuilder().append("\"").append(addEscapes(String.valueOf(curChar1))).append("\"").append(" (").append(curChar).append("), ").toString()) + "after : \"" + addEscapes(errorAfter) + "\"";
  }
  








  public String getMessage()
  {
    return super.getMessage();
  }
  



  public TokenMgrError() {}
  



  public TokenMgrError(String message, int reason)
  {
    super(message);
    errorCode = reason;
  }
  
  public TokenMgrError(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar, int reason)
  {
    this(LexicalErr(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
  }
}
