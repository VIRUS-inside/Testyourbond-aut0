package com.steadystate.css.parser;



public abstract interface SACParserCSS1Constants
{
  public static final int EOF = 0;
  

  public static final int S = 1;
  

  public static final int IDENT = 3;
  

  public static final int LINK_PSCLASS = 4;
  

  public static final int VISITED_PSCLASS = 5;
  

  public static final int ACTIVE_PSCLASS = 6;
  

  public static final int FIRST_LINE = 7;
  

  public static final int FIRST_LETTER = 8;
  
  public static final int HASH = 9;
  
  public static final int LBRACE = 10;
  
  public static final int RBRACE = 11;
  
  public static final int COMMA = 12;
  
  public static final int DOT = 13;
  
  public static final int SEMICOLON = 14;
  
  public static final int COLON = 15;
  
  public static final int SLASH = 16;
  
  public static final int PLUS = 17;
  
  public static final int MINUS = 18;
  
  public static final int EQUALS = 19;
  
  public static final int GT = 20;
  
  public static final int LSQUARE = 21;
  
  public static final int RSQUARE = 22;
  
  public static final int STRING = 23;
  
  public static final int RROUND = 24;
  
  public static final int URL = 25;
  
  public static final int CDO = 26;
  
  public static final int CDC = 27;
  
  public static final int IMPORT_SYM = 28;
  
  public static final int IMPORTANT_SYM = 29;
  
  public static final int ATKEYWORD = 30;
  
  public static final int EMS = 31;
  
  public static final int EXS = 32;
  
  public static final int LENGTH_PX = 33;
  
  public static final int LENGTH_CM = 34;
  
  public static final int LENGTH_MM = 35;
  
  public static final int LENGTH_IN = 36;
  
  public static final int LENGTH_PT = 37;
  
  public static final int LENGTH_PC = 38;
  
  public static final int PERCENTAGE = 39;
  
  public static final int NUMBER = 40;
  
  public static final int RGB = 41;
  
  public static final int NAME = 42;
  
  public static final int D = 43;
  
  public static final int NUM = 44;
  
  public static final int UNICODERANGE = 45;
  
  public static final int RANGE = 46;
  
  public static final int Q16 = 47;
  
  public static final int Q15 = 48;
  
  public static final int Q14 = 49;
  
  public static final int Q13 = 50;
  
  public static final int Q12 = 51;
  
  public static final int Q11 = 52;
  
  public static final int LATIN1 = 53;
  
  public static final int NMCHAR = 54;
  
  public static final int NMSTART = 55;
  
  public static final int STRING1 = 56;
  
  public static final int STRING2 = 57;
  
  public static final int NONASCII = 58;
  
  public static final int ESCAPE = 59;
  
  public static final int NL = 60;
  
  public static final int UNICODE = 61;
  
  public static final int HNUM = 62;
  
  public static final int H = 63;
  
  public static final int UNKNOWN = 66;
  
  public static final int DEFAULT = 0;
  
  public static final int COMMENT = 1;
  
  public static final String[] tokenImage = { "<EOF>", "<S>", "\"/*\"", "<IDENT>", "\":link\"", "\":visited\"", "\":active\"", "\":first-line\"", "\":first-letter\"", "<HASH>", "\"{\"", "\"}\"", "\",\"", "\".\"", "\";\"", "\":\"", "\"/\"", "\"+\"", "\"-\"", "\"=\"", "\">\"", "\"[\"", "\"]\"", "<STRING>", "\")\"", "<URL>", "\"<!--\"", "\"-->\"", "\"@import\"", "<IMPORTANT_SYM>", "<ATKEYWORD>", "<EMS>", "<EXS>", "<LENGTH_PX>", "<LENGTH_CM>", "<LENGTH_MM>", "<LENGTH_IN>", "<LENGTH_PT>", "<LENGTH_PC>", "<PERCENTAGE>", "<NUMBER>", "\"rgb(\"", "<NAME>", "<D>", "<NUM>", "<UNICODERANGE>", "<RANGE>", "<Q16>", "<Q15>", "<Q14>", "<Q13>", "<Q12>", "\"?\"", "<LATIN1>", "<NMCHAR>", "<NMSTART>", "<STRING1>", "<STRING2>", "<NONASCII>", "<ESCAPE>", "<NL>", "<UNICODE>", "<HNUM>", "<H>", "\"*/\"", "<token of kind 65>", "<UNKNOWN>" };
}
