package com.steadystate.css.parser;

import java.io.IOException;

public abstract interface CharStream
{
  public abstract char readChar()
    throws IOException;
  
  @Deprecated
  public abstract int getColumn();
  
  @Deprecated
  public abstract int getLine();
  
  public abstract int getEndColumn();
  
  public abstract int getEndLine();
  
  public abstract int getBeginColumn();
  
  public abstract int getBeginLine();
  
  public abstract void backup(int paramInt);
  
  public abstract char BeginToken()
    throws IOException;
  
  public abstract String GetImage();
  
  public abstract char[] GetSuffix(int paramInt);
  
  public abstract void Done();
  
  public abstract void setTabSize(int paramInt);
  
  public abstract int getTabSize();
  
  public abstract boolean getTrackLineColumn();
  
  public abstract void setTrackLineColumn(boolean paramBoolean);
}
