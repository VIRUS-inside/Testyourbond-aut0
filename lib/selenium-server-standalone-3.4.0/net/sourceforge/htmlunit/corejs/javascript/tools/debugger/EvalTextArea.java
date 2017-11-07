package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;













































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































class EvalTextArea
  extends JTextArea
  implements KeyListener, DocumentListener
{
  private static final long serialVersionUID = -3918033649601064194L;
  private SwingGui debugGui;
  private List<String> history;
  private int historyIndex = -1;
  


  private int outputMark;
  



  public EvalTextArea(SwingGui debugGui)
  {
    this.debugGui = debugGui;
    history = Collections.synchronizedList(new ArrayList());
    Document doc = getDocument();
    doc.addDocumentListener(this);
    addKeyListener(this);
    setLineWrap(true);
    setFont(new Font("Monospaced", 0, 12));
    append("% ");
    outputMark = doc.getLength();
  }
  




  public void select(int start, int end)
  {
    super.select(start, end);
  }
  


  private synchronized void returnPressed()
  {
    Document doc = getDocument();
    int len = doc.getLength();
    Segment segment = new Segment();
    try {
      doc.getText(outputMark, len - outputMark, segment);
    } catch (BadLocationException ignored) {
      ignored.printStackTrace();
    }
    String text = segment.toString();
    if (debugGui.dim.stringIsCompilableUnit(text)) {
      if (text.trim().length() > 0) {
        history.add(text);
        historyIndex = history.size();
      }
      append("\n");
      String result = debugGui.dim.eval(text);
      if (result.length() > 0) {
        append(result);
        append("\n");
      }
      append("% ");
      outputMark = doc.getLength();
    } else {
      append("\n");
    }
  }
  


  public synchronized void write(String str)
  {
    insert(str, outputMark);
    int len = str.length();
    outputMark += len;
    select(outputMark, outputMark);
  }
  




  public void keyPressed(KeyEvent e)
  {
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
  


  public void keyTyped(KeyEvent e)
  {
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
  




  public synchronized void insertUpdate(DocumentEvent e)
  {
    int len = e.getLength();
    int off = e.getOffset();
    if (outputMark > off) {
      outputMark += len;
    }
  }
  


  public synchronized void removeUpdate(DocumentEvent e)
  {
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
    setCaret(getCaret());
    select(outputMark, outputMark);
  }
  
  public synchronized void changedUpdate(DocumentEvent e) {}
}
