package org.yaml.snakeyaml.reader;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.scanner.Constant;



















public class StreamReader
{
  public static final Pattern NON_PRINTABLE = Pattern.compile("[^\t\n\r -~ -퟿-�]");
  
  private String name;
  private final Reader stream;
  private int pointer = 0;
  private boolean eof = true;
  private String buffer;
  private int index = 0;
  private int line = 0;
  private int column = 0;
  private char[] data;
  
  public StreamReader(String stream) {
    name = "'string'";
    buffer = "";
    checkPrintable(stream);
    buffer = (stream + "\000");
    this.stream = null;
    eof = true;
    data = null;
  }
  
  public StreamReader(Reader reader) {
    name = "'reader'";
    buffer = "";
    stream = reader;
    eof = false;
    data = new char['Ѐ'];
    update();
  }
  
  void checkPrintable(CharSequence data) {
    Matcher em = NON_PRINTABLE.matcher(data);
    if (em.find()) {
      int position = index + buffer.length() - pointer + em.start();
      throw new ReaderException(name, position, em.group().charAt(0), "special characters are not allowed");
    }
  }
  












  void checkPrintable(char[] chars, int begin, int end)
  {
    for (int i = begin; i < end; i++) {
      char c = chars[i];
      
      if (!isPrintable(c))
      {


        int position = index + buffer.length() - pointer + i;
        throw new ReaderException(name, position, c, "special characters are not allowed");
      }
    }
  }
  
  public static boolean isPrintable(char c) { return ((c >= ' ') && (c <= '~')) || (c == '\n') || (c == '\r') || (c == '\t') || (c == '') || ((c >= ' ') && (c <= 55295)) || ((c >= 57344) && (c <= 65533)); }
  


  public Mark getMark()
  {
    return new Mark(name, index, line, column, buffer, pointer);
  }
  
  public void forward() {
    forward(1);
  }
  




  public void forward(int length)
  {
    if (pointer + length + 1 >= buffer.length()) {
      update();
    }
    char ch = '\000';
    for (int i = 0; i < length; i++) {
      ch = buffer.charAt(pointer);
      pointer += 1;
      index += 1;
      if ((Constant.LINEBR.has(ch)) || ((ch == '\r') && (buffer.charAt(pointer) != '\n'))) {
        line += 1;
        column = 0;
      } else if (ch != 65279) {
        column += 1;
      }
    }
  }
  
  public char peek() {
    return buffer.charAt(pointer);
  }
  





  public char peek(int index)
  {
    if (pointer + index + 1 > buffer.length()) {
      update();
    }
    return buffer.charAt(pointer + index);
  }
  





  public String prefix(int length)
  {
    if (pointer + length >= buffer.length()) {
      update();
    }
    if (pointer + length > buffer.length()) {
      return buffer.substring(pointer);
    }
    return buffer.substring(pointer, pointer + length);
  }
  


  public String prefixForward(int length)
  {
    String prefix = prefix(length);
    pointer += length;
    index += length;
    
    column += length;
    return prefix;
  }
  
  private void update() {
    if (!eof) {
      buffer = buffer.substring(pointer);
      pointer = 0;
      try {
        int converted = stream.read(data);
        if (converted > 0)
        {





          checkPrintable(data, 0, converted);
          buffer = new StringBuilder(buffer.length() + converted).append(buffer).append(data, 0, converted).toString();
        }
        else {
          eof = true;
          buffer += "\000";
        }
      } catch (IOException ioe) {
        throw new YAMLException(ioe);
      }
    }
  }
  
  public int getColumn() {
    return column;
  }
  
  public Charset getEncoding() {
    return Charset.forName(((UnicodeReader)stream).getEncoding());
  }
  
  public int getIndex() {
    return index;
  }
  
  public int getLine() {
    return line;
  }
}
