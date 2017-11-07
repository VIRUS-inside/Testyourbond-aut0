package org.openqa.selenium.remote;

import java.util.Optional;
import java.util.function.Function;

abstract interface HandshakeResponse
{
  public abstract Function<InitialHandshakeResponse, Optional<ProtocolHandshake.Result>> getResponseFunction();
}
