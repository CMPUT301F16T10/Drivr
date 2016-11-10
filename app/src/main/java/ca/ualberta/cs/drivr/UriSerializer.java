package ca.ualberta.cs.drivr;

import android.net.Uri;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * A class to use with Gson for serializing/deserializing Uris.
 * <ul>
 *     <li>URL: http://stackoverflow.com/a/27704368</li>
 *     <li>Author: Marco Bonechi</li>
 *     <li>Accessed: November 9, 2016</li>
 * </ul>
 */
public class UriSerializer implements JsonSerializer<Uri>, JsonDeserializer<Uri> {
    @Override
    public JsonElement serialize(Uri src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Uri deserialize(final JsonElement src, final Type srcType,
                           final JsonDeserializationContext context) throws JsonParseException {
        return Uri.parse(src.getAsString());
    }
}
