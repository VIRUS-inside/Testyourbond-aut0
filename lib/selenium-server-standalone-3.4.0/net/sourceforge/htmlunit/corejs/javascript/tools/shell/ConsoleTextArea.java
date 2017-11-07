package net.sourceforge.htmlunit.corejs.javascript.tools.shell;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;























































public class ConsoleTextArea
  extends JTextArea
  implements KeyListener, DocumentListener
{
  static final long serialVersionUID = 8557083244830872961L;
  private ConsoleWriter console1;
  private ConsoleWriter console2;
  private PrintStream out;
  private PrintStream err;
  private PrintWriter inPipe;
  private PipedInputStream in;
  private List<String> history;
  private int historyIndex = -1;
  private int outputMark = 0;
  
  public void select(int start, int end)
  {
    requestFocus();
    super.select(start, end);
  }
  
  public ConsoleTextArea(String[] argv)
  {
    history = new ArrayList();
    console1 = new ConsoleWriter(this);
    console2 = new ConsoleWriter(this);
    out = new PrintStream(console1, true);
    err = new PrintStream(console2, true);
    PipedOutputStream outPipe = new PipedOutputStream();
    inPipe = new PrintWriter(outPipe);
    in = new PipedInputStream();
    try {
      outPipe.connect(in);
    } catch (IOException exc) {
      exc.printStackTrace();
    }
    getDocument().addDocumentListener(this);
    addKeyListener(this);
    setLineWrap(true);
    setFont(new Font("Monospaced", 0, 12));
  }
  
  synchronized void returnPressed() {
    Document doc = getDocument();
    int len = doc.getLength();
    Segment segment = new Segment();
    try {
      doc.getText(outputMark, len - outputMark, segment);
    } catch (BadLocationException ignored) {
      ignored.printStackTrace();
    }
    if (count > 0) {
      history.add(segment.toString());
    }
    historyIndex = history.size();
    inPipe.write(array, offset, count);
    append("\n");
    outputMark = doc.getLength();
    inPipe.write("\n");
    inPipe.flush();
    console1.flush();
  }
  
  public void eval(String str) {
    inPipe.write(str);
    inPipe.write("\n");
    inPipe.flush();
    console1.flush();
  }
  
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if ((code == 8) || (code == 37)) {
      if (outputMark == getCaretPosition()) {
        e.consume();
      }
    } else if (code == 36) {
      int caretPos = getCaretPosition();
      if (caretPos == outputMark) {
        e.consume();
      } else if ((caretPos > outputMark) && 
        (!e.isControlDown())) {
        if (e.isShiftDown()) {
          moveCaretPosition(outputMark);
        } else {
          setCaretPosition(outputMark);
        }
        e.consume();
      }
    }
    else if (code == 10) {
      returnPressed();
      e.consume();
    } else if (code == 38) {
      historyIndex -= 1;
      if (historyIndex >= 0) {
        if (historyIndex >= history.size()) {
          historyIndex = (history.size() - 1);
        }
        if (historyIndex >= 0) {
          String str = (String)history.get(historyIndex);
          int len = getDocument().getLength();
          replaceRange(str, outputMark, len);
          int caretPos = outputMark + str.length();
          select(caretPos, caretPos);
        } else {
          historyIndex += 1;
        }
      } else {
        historyIndex += 1;
      }
      e.consume();
    } else if (code == 40) {
      int caretPos = outputMark;
      if (history.size() > 0) {
        historyIndex += 1;
        if (historyIndex < 0) {
          historyIndex = 0;
        }
        int len = getDocument().getLength();
        if (historyIndex < history.size()) {
          String str = (String)history.get(historyIndex);
          replaceRange(str, outputMark, len);
          caretPos = outputMark + str.length();
        } else {
          historyIndex = history.size();
          replaceRange("", outputMark, len);
        }
      }
      select(caretPos, caretPos);
      e.consume();
    }
  }
  
  public void keyTyped(KeyEvent e) {
    int keyChar = e.getKeyChar();
    if (keyChar == 8) {
      if (outputMark == getCaretPosition()) {
        e.consume();
      }
    } else if (getCaretPosition() < outputMark) {
      setCaretPosition(outputMark);
    }
  }
  
  public synchronized void keyReleased(KeyEvent e) {}
  
  public synchronized void write(String str)
  {
    insert(str, outputMark);
    int len = str.length();
    outputMark += len;
    select(outputMark, outputMark);
  }
  
  public synchronized void insertUpdate(DocumentEvent e) {
    int len = e.getLength();
    int off = e.getOffset();
    if (outputMark > off) {
      outputMark += len;
    }
  }
  
  public synchronized void removeUpdate(DocumentEvent e) {
    int len = e.getLength();
    int off = e.getOffset();
    if (outputMark > off) {
      if (outputMark >= off + len) {
        outputMark -= len;
      } else {
        outputMark = off;
      }
    }
  }
  
  public synchronized void postUpdateUI()
  {
    requestFocus();
    setCaret(getCaret());
    select(outputMark, outputMark);
  }
  
  public synchronized void changedUpdate(DocumentEvent e) {}
  
  public InputStream getIn()
  {
    return in;
  }
  
  public PrintStream getOut() {
    return out;
  }
  
  public PrintStream getErr() {
    return err;
  }
}
