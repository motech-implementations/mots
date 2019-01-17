package org.motechproject.mots.utils;

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class TrimTextDeserializer extends JsonDeserializer<String> {

  @Override
  public String deserialize(JsonParser jsonParser, DeserializationContext context)
      throws IOException {

    return jsonParser.hasToken(VALUE_STRING) && jsonParser.getText().trim().length() > 0
        ? jsonParser.getText().trim() : null;
  }
}
