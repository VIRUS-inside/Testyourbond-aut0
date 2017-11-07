package org.openqa.grid.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.openqa.grid.common.exception.GridConfigurationException;






















public class JSONConfigurationUtils
{
  public JSONConfigurationUtils() {}
  
  public static JsonObject loadJSON(String resource)
  {
    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(JSONConfigurationUtils.class
      .getPackage().getName().replace('.', '/') + '/' + resource);
    
    if (in == null) {
      try {
        in = new FileInputStream(resource);
      }
      catch (FileNotFoundException localFileNotFoundException) {}
    }
    

    if (in == null) {
      throw new RuntimeException(resource + " is not a valid resource.");
    }
    
    b = new StringBuilder();
    InputStreamReader inputreader = new InputStreamReader(in);
    BufferedReader buffreader = new BufferedReader(inputreader);
    try
    {
      String line;
      while ((line = buffreader.readLine()) != null) {
        b.append(line);
      }
      

      try
      {
        buffreader.close();
        inputreader.close();
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      try
      {
        String line;
        return new JsonParser().parse(b.toString()).getAsJsonObject();
      } catch (JsonSyntaxException e) {
        throw new GridConfigurationException("Wrong format for the JSON input : " + e.getMessage(), e);




















      }
      





















    }
    catch (IOException e)
    {




















      throw new GridConfigurationException("Cannot read file " + resource + " , " + e.getMessage(), e);
    } finally {
      try {
        buffreader.close();
        inputreader.close();
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
