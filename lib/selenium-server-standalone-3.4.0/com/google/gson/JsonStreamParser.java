package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;








































public final class JsonStreamParser
  implements Iterator<JsonElement>
{
  private final JsonReader parser;
  private final Object lock;
  
  public JsonStreamParser(String json)
  {
    this(new StringReader(json));
  }
  



  public JsonStreamParser(Reader reader)
  {
    parser = new JsonReader(reader);
    parser.setLenient(true);
    lock = new Object();
  }
  





  public JsonElement next()
    throws JsonParseException
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    try
    {
      return Streams.parse(parser);
    } catch (StackOverflowError e) {
      throw new JsonParseException("Failed parsing JSON source to Json", e);
    } catch (OutOfMemoryError e) {
      throw new JsonParseException("Failed parsing JSON source to Json", e);
    } catch (JsonParseException e) {
      throw ((e.getCause() instanceof EOFException) ? new NoSuchElementException() : e);
    }
  }
  




  public boolean hasNext()
  {
    synchronized (lock) {
      try {
        return parser.peek() != JsonToken.END_DOCUMENT;
      } catch (MalformedJsonException e) {
        throw new JsonSyntaxException(e);
      } catch (IOException e) {
        throw new JsonIOException(e);
      }
    }
  }
  




  public void remove()
  {
    throw new UnsupportedOperationException();
  }
}
