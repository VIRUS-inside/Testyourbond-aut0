package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.io.OutputStream;
import javax.swing.SwingUtilities;























class ConsoleWriter
  extends OutputStream
{
  private ConsoleTextArea textArea;
  private StringBuffer buffer;
  
  public ConsoleWriter(ConsoleTextArea textArea)
  {
    this.textArea = textArea;
    buffer = new StringBuffer();
  }
  
  public synchronized void write(int ch)
  {
    buffer.append((char)ch);
    if (ch == 10) {
      flushBuffer();
    }
  }
  
  public synchronized void write(char[] data, int off, int len) {
    for (int i = off; i < len; i++) {
      buffer.append(data[i]);
      if (data[i] == '\n') {
        flushBuffer();
      }
    }
  }
  
  public synchronized void flush()
  {
    if (buffer.length() > 0) {
      flushBuffer();
    }
  }
  
  public void close()
  {
    flush();
  }
  
  private void flushBuffer() {
    String str = buffer.toString();
    buffer.setLength(0);
    SwingUtilities.invokeLater(new ConsoleWrite(textArea, str));
  }
}
