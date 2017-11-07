package org.openqa.selenium.remote.http;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.logging.Logger;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.ErrorCodes;
import org.openqa.selenium.remote.JsonToBeanConverter;
import org.openqa.selenium.remote.Response;












































public class W3CHttpResponseCodec
  extends AbstractHttpResponseCodec
{
  private static final Logger log = Logger.getLogger(W3CHttpResponseCodec.class.getName());
  
  private final ErrorCodes errorCodes = new ErrorCodes();
  private final JsonToBeanConverter jsonToBeanConverter = new JsonToBeanConverter();
  
  public W3CHttpResponseCodec() {}
  
  public Response decode(HttpResponse encodedResponse) { String content = encodedResponse.getContentString().trim();
    log.fine(String.format("Decoding response. Response code was: %d and content: %s", new Object[] {
    
      Integer.valueOf(encodedResponse.getStatus()), content }));
    
    String contentType = Strings.nullToEmpty(encodedResponse.getHeader("Content-Type"));
    
    Response response = new Response();
    


    if (200 != encodedResponse.getStatus()) {
      log.fine("Processing an error");
      JsonObject obj = new JsonParser().parse(content).getAsJsonObject();
      
      JsonElement w3cWrappedValue = obj.get("value");
      if (((w3cWrappedValue instanceof JsonObject)) && (((JsonObject)w3cWrappedValue).has("error"))) {
        obj = (JsonObject)w3cWrappedValue;
      }
      
      String message = "An unknown error has occurred";
      if (obj.has("message")) {
        message = obj.get("message").getAsString();
      }
      
      String error = "unknown error";
      if (obj.has("error")) {
        error = obj.get("error").getAsString();
      }
      
      response.setState(error);
      response.setStatus(Integer.valueOf(errorCodes.toStatus(error, Optional.of(Integer.valueOf(encodedResponse.getStatus())))));
      

      if (("unexpected alert open".equals(error)) && 
        (500 == encodedResponse.getStatus())) {
        String text = "";
        JsonElement data = obj.get("data");
        if (data != null) {
          JsonElement rawText = data.getAsJsonObject().get("text");
          if (rawText != null) {
            text = rawText.getAsString();
          }
        }
        response.setValue(new UnhandledAlertException(message, text));
      } else {
        response.setValue(createException(error, message));
      }
      return response;
    }
    
    response.setState("success");
    response.setStatus(Integer.valueOf(0));
    if ((encodedResponse.getContent().length > 0) && (
      (contentType.startsWith("application/json")) || (Strings.isNullOrEmpty("")))) {
      JsonObject parsed = new JsonParser().parse(content).getAsJsonObject();
      if (parsed.has("value")) {
        Object value = jsonToBeanConverter.convert(Object.class, parsed.get("value"));
        response.setValue(value);
      }
      else {
        response.setValue(jsonToBeanConverter.convert(Object.class, content));
      }
    }
    

    if ((response.getValue() instanceof String))
    {


      response.setValue(((String)response.getValue()).replace("\r\n", "\n"));
    }
    
    return response;
  }
  
  private WebDriverException createException(String error, String message) {
    Class<? extends WebDriverException> clazz = errorCodes.getExceptionType(error);
    try
    {
      Constructor<? extends WebDriverException> constructor = clazz.getConstructor(new Class[] { String.class });
      return (WebDriverException)constructor.newInstance(new Object[] { message });
    } catch (ReflectiveOperationException e) {
      throw new WebDriverException(message);
    }
  }
}
