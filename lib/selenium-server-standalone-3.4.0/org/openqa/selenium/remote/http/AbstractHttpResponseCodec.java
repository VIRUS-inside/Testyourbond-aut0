package org.openqa.selenium.remote.http;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.net.MediaType;
import java.util.Optional;
import org.openqa.selenium.remote.BeanToJsonConverter;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.JsonException;
import org.openqa.selenium.remote.JsonToBeanConverter;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.ResponseCodec;





























public abstract class AbstractHttpResponseCodec
  implements ResponseCodec<HttpResponse>
{
  private final ErrorCodes errorCodes = new ErrorCodes();
  private final BeanToJsonConverter beanToJsonConverter = new BeanToJsonConverter();
  private final JsonToBeanConverter jsonToBeanConverter = new JsonToBeanConverter();
  


  public AbstractHttpResponseCodec() {}
  


  public HttpResponse encode(Response response)
  {
    int status = response.getStatus().intValue() == 0 ? 200 : 500;
    


    byte[] data = beanToJsonConverter.convert(response).getBytes(Charsets.UTF_8);
    
    HttpResponse httpResponse = new HttpResponse();
    httpResponse.setStatus(status);
    httpResponse.setHeader("Cache-Control", "no-cache");
    httpResponse.setHeader("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");
    httpResponse.setHeader("Content-Length", String.valueOf(data.length));
    httpResponse.setHeader("Content-Type", MediaType.JSON_UTF_8.toString());
    httpResponse.setContent(data);
    
    return httpResponse;
  }
  
  public Response decode(HttpResponse encodedResponse)
  {
    String contentType = Strings.nullToEmpty(encodedResponse.getHeader("Content-Type"));
    String content = encodedResponse.getContentString().trim();
    try {
      return (Response)jsonToBeanConverter.convert(Response.class, content);
    } catch (JsonException e) {
      if (contentType.startsWith("application/json")) {
        throw new IllegalArgumentException("Cannot decode response content: " + content, e);
      }
    }
    catch (ClassCastException e) {
      if (contentType.startsWith("application/json")) {
        if (content.isEmpty())
        {



          return new Response();
        }
        throw new IllegalArgumentException("Cannot decode response content: " + content, e);
      }
    }
    

    Response response = new Response();
    int statusCode = encodedResponse.getStatus();
    if ((statusCode < 200) || (statusCode > 299))
    {
      if ((statusCode > 399) && (statusCode < 500)) {
        response.setStatus(Integer.valueOf(9));
      } else {
        response.setStatus(Integer.valueOf(13));
      }
    }
    
    if (encodedResponse.getContent().length > 0) {
      response.setValue(content);
    }
    
    if ((response.getValue() instanceof String))
    {


      response.setValue(((String)response.getValue()).replace("\r\n", "\n"));
    }
    
    if ((response.getStatus() != null) && (response.getState() == null)) {
      response.setState(errorCodes.toState(response.getStatus()));
    } else if ((response.getStatus() == null) && (response.getState() != null)) {
      response.setStatus(
        Integer.valueOf(errorCodes.toStatus(response.getState(), 
        Optional.of(Integer.valueOf(encodedResponse.getStatus())))));
    } else if (statusCode == 200) {
      response.setStatus(Integer.valueOf(0));
      response.setState(errorCodes.toState(Integer.valueOf(0)));
    }
    
    if (response.getStatus() != null) {
      response.setState(errorCodes.toState(response.getStatus()));
    } else if (statusCode == 200) {
      response.setState(errorCodes.toState(Integer.valueOf(0)));
    }
    return response;
  }
}
