package org.seleniumhq.jetty9.http;





public abstract interface HttpTokens
{
  public static final byte COLON = 58;
  



  public static final byte TAB = 9;
  



  public static final byte LINE_FEED = 10;
  



  public static final byte CARRIAGE_RETURN = 13;
  


  public static final byte SPACE = 32;
  


  public static final byte[] CRLF = { 13, 10 };
  public static final byte SEMI_COLON = 59;
  
  public static enum EndOfContent { UNKNOWN_CONTENT,  NO_CONTENT,  EOF_CONTENT,  CONTENT_LENGTH,  CHUNKED_CONTENT;
    
    private EndOfContent() {}
  }
}
