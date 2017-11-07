package org.eclipse.jetty.websocket.client;

import java.io.IOException;
import java.util.List;
import org.eclipse.jetty.client.HttpResponse;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.common.UpgradeResponseAdapter;























public class ClientUpgradeResponse
  extends UpgradeResponseAdapter
{
  private List<ExtensionConfig> extensions;
  
  public ClientUpgradeResponse() {}
  
  public ClientUpgradeResponse(HttpResponse response)
  {
    setStatusCode(response.getStatus());
    setStatusReason(response.getReason());
    
    HttpFields fields = response.getHeaders();
    for (HttpField field : fields)
    {
      addHeader(field.getName(), field.getValue());
    }
    
    HttpField extensionsField = fields.getField(HttpHeader.SEC_WEBSOCKET_EXTENSIONS);
    if (extensionsField != null)
      extensions = ExtensionConfig.parseList(extensionsField.getValues());
    setAcceptedSubProtocol(fields.get(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL));
  }
  

  public List<ExtensionConfig> getExtensions()
  {
    return extensions;
  }
  
  public void sendForbidden(String message)
    throws IOException
  {
    throw new UnsupportedOperationException("Not supported on client implementation");
  }
}
