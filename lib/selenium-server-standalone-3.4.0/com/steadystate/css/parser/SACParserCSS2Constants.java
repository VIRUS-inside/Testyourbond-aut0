package com.steadystate.css.parser;



public abstract interface SACParserCSS2Constants
{
  public static final int EOF = 0;
  

  public static final int S = 1;
  

  public static final int W = 2;
  

  public static final int LBRACE = 6;
  

  public static final int RBRACE = 7;
  

  public static final int COMMA = 8;
  

  public static final int DOT = 9;
  

  public static final int SEMICOLON = 10;
  
  public static final int COLON = 11;
  
  public static final int ASTERISK = 12;
  
  public static final int SLASH = 13;
  
  public static final int PLUS = 14;
  
  public static final int MINUS = 15;
  
  public static final int EQUALS = 16;
  
  public static final int GT = 17;
  
  public static final int LSQUARE = 18;
  
  public static final int RSQUARE = 19;
  
  public static final int HASH = 20;
  
  public static final int STRING = 21;
  
  public static final int RROUND = 22;
  
  public static final int URL = 23;
  
  public static final int URI = 24;
  
  public static final int CDO = 25;
  
  public static final int CDC = 26;
  
  public static final int INCLUDES = 27;
  
  public static final int DASHMATCH = 28;
  
  public static final int IMPORT_SYM = 29;
  
  public static final int PAGE_SYM = 30;
  
  public static final int MEDIA_SYM = 31;
  
  public static final int FONT_FACE_SYM = 32;
  
  public static final int CHARSET_SYM = 33;
  
  public static final int ATKEYWORD = 34;
  
  public static final int IMPORTANT_SYM = 35;
  
  public static final int INHERIT = 36;
  
  public static final int EMS = 37;
  
  public static final int EXS = 38;
  
  public static final int LENGTH_PX = 39;
  
  public static final int LENGTH_CM = 40;
  
  public static final int LENGTH_MM = 41;
  
  public static final int LENGTH_IN = 42;
  
  public static final int LENGTH_PT = 43;
  
  public static final int LENGTH_PC = 44;
  
  public static final int ANGLE_DEG = 45;
  
  public static final int ANGLE_RAD = 46;
  
  public static final int ANGLE_GRAD = 47;
  
  public static final int TIME_MS = 48;
  
  public static final int TIME_S = 49;
  
  public static final int FREQ_HZ = 50;
  
  public static final int FREQ_KHZ = 51;
  
  public static final int PERCENTAGE = 52;
  
  public static final int DIMEN = 53;
  
  public static final int NUMBER = 54;
  
  public static final int RGB = 55;
  
  public static final int FUNCTION_LANG = 56;
  
  public static final int FUNCTION = 57;
  
  public static final int IDENT = 58;
  
  public static final int NAME = 59;
  
  public static final int NUM = 60;
  
  public static final int UNICODERANGE = 61;
  
  public static final int RANGE = 62;
  
  public static final int Q16 = 63;
  
  public static final int Q15 = 64;
  
  public static final int Q14 = 65;
  
  public static final int Q13 = 66;
  
  public static final int Q12 = 67;
  
  public static final int Q11 = 68;
  
  public static final int NMSTART = 69;
  
  public static final int NMCHAR = 70;
  
  public static final int STRING1 = 71;
  
  public static final int STRING2 = 72;
  
  public static final int NONASCII = 73;
  
  public static final int ESCAPE = 74;
  
  public static final int NL = 75;
  
  public static final int UNICODE = 76;
  
  public static final int HNUM = 77;
  
  public static final int H = 78;
  
  public static final int UNKNOWN = 79;
  
  public static final int DEFAULT = 0;
  
  public static final int COMMENT = 1;
  
  public static final String[] tokenImage = { "<EOF>", "<S>", "<W>", "\"/*\"", "\"*/\"", "<token of kind 5>", "\"{\"", "\"}\"", "\",\"", "\".\"", "\";\"", "\":\"", "\"*\"", "\"/\"", "\"+\"", "\"-\"", "\"=\"", "\">\"", "\"[\"", "\"]\"", "<HASH>", "<STRING>", "\")\"", "<URL>", "<URI>", "\"<!--\"", "\"-->\"", "\"~=\"", "\"|=\"", "\"@import\"", "\"@page\"", "\"@media\"", "\"@font-face\"", "\"@charset\"", "<ATKEYWORD>", "<IMPORTANT_SYM>", "\"inherit\"", "<EMS>", "<EXS>", "<LENGTH_PX>", "<LENGTH_CM>", "<LENGTH_MM>", "<LENGTH_IN>", "<LENGTH_PT>", "<LENGTH_PC>", "<ANGLE_DEG>", "<ANGLE_RAD>", "<ANGLE_GRAD>", "<TIME_MS>", "<TIME_S>", "<FREQ_HZ>", "<FREQ_KHZ>", "<PERCENTAGE>", "<DIMEN>", "<NUMBER>", "\"rgb(\"", "\"lang(\"", "<FUNCTION>", "<IDENT>", "<NAME>", "<NUM>", "<UNICODERANGE>", "<RANGE>", "<Q16>", "<Q15>", "<Q14>", "<Q13>", "<Q12>", "\"?\"", "<NMSTART>", "<NMCHAR>", "<STRING1>", "<STRING2>", "<NONASCII>", "<ESCAPE>", "<NL>", "<UNICODE>", "<HNUM>", "<H>", "<UNKNOWN>" };
}
