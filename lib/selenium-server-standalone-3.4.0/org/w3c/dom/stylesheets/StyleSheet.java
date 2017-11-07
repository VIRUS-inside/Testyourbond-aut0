package org.w3c.dom.stylesheets;

import org.w3c.dom.Node;

public abstract interface StyleSheet
{
  public abstract String getType();
  
  public abstract boolean getDisabled();
  
  public abstract void setDisabled(boolean paramBoolean);
  
  public abstract Node getOwnerNode();
  
  public abstract StyleSheet getParentStyleSheet();
  
  public abstract String getHref();
  
  public abstract String getTitle();
  
  public abstract MediaList getMedia();
}
