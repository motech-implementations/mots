package org.motechproject.mots.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.motechproject.mots.dto.VotoBlockResponseDto;

public class VotoBlockResponseDtoDeserializer extends JsonDeserializer<VotoBlockResponseDto> {

  @Override
  public VotoBlockResponseDto deserialize(JsonParser jp,
      DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);

    if (node.isArray()) {
      return null;
    }

    return oc.readValue(oc.treeAsTokens(node), VotoBlockResponseDto.class);
  }
}
