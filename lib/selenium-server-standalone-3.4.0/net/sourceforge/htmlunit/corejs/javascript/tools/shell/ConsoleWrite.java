package net.sourceforge.htmlunit.corejs.javascript.tools.shell;





class ConsoleWrite
  implements Runnable
{
  private ConsoleTextArea textArea;
  


  private String str;
  



  public ConsoleWrite(ConsoleTextArea textArea, String str)
  {
    this.textArea = textArea;
    this.str = str;
  }
  
  public void run() {
    textArea.write(str);
  }
}
