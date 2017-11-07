package net.sourceforge.htmlunit.corejs.javascript.regexp;






public class SubString
{
  public SubString() {}
  




  public SubString(String str)
  {
    this.str = str;
    index = 0;
    length = str.length();
  }
  
  public SubString(String source, int start, int len) {
    str = source;
    index = start;
    length = len;
  }
  
  public String toString()
  {
    return str == null ? "" : str.substring(index, index + length);
  }
  
  public static final SubString emptySubString = new SubString();
  String str;
  int index;
  int length;
}
