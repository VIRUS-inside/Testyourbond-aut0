package org.eclipse.jetty.websocket.common.frames;

import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.extensions.Frame.Type;


















public class CloseFrame
  extends ControlFrame
{
  public CloseFrame()
  {
    super((byte)8);
  }
  

  public Frame.Type getType()
  {
    return Frame.Type.CLOSE;
  }
  







  public static String truncate(String reason)
  {
    return StringUtil.truncate(reason, 123);
  }
}
