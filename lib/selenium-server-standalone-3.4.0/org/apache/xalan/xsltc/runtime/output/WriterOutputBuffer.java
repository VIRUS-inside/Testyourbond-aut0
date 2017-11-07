package org.apache.xalan.xsltc.runtime.output;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;























class WriterOutputBuffer
  implements OutputBuffer
{
  private static final int KB = 1024;
  private static int BUFFER_SIZE = 4096;
  private Writer _writer;
  
  static {
    String osName = System.getProperty("os.name");
    if (osName.equalsIgnoreCase("solaris")) {
      BUFFER_SIZE = 32768;
    }
  }
  







  public WriterOutputBuffer(Writer writer)
  {
    _writer = new BufferedWriter(writer, BUFFER_SIZE);
  }
  
  public String close() {
    try {
      _writer.flush();
    }
    catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
    return "";
  }
  
  public OutputBuffer append(String s) {
    try {
      _writer.write(s);
    }
    catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
    return this;
  }
  
  public OutputBuffer append(char[] s, int from, int to) {
    try {
      _writer.write(s, from, to);
    }
    catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
    return this;
  }
  
  public OutputBuffer append(char ch) {
    try {
      _writer.write(ch);
    }
    catch (IOException e) {
      throw new RuntimeException(e.toString());
    }
    return this;
  }
}
