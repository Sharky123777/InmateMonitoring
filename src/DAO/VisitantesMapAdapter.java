package DAO;

import Model.Entities.Visitante;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class VisitantesMapAdapter implements JsonSerializer<Map<Visitante, String>>,
        JsonDeserializer<Map<Visitante, String>> {

    @Override
    public JsonElement serialize(Map<Visitante, String> src, Type typeOfSrc,
            JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        for (Map.Entry<Visitante, String> entry : src.entrySet()) {
            result.add(entry.getKey().getIdentificacion(),
                    new JsonPrimitive(entry.getValue()));
        }
        return result;
    }

    @Override
    public Map<Visitante, String> deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        Map<Visitante, String> result = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();

        VisitanteDAO visitanteDAO = VisitanteDAO.getInstancia();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String identificacion = entry.getKey();
            String relacion = entry.getValue().getAsString();

            Visitante visitante = visitanteDAO.buscarVisitantePorIdentificacion(identificacion);
            if (visitante != null) {
                result.put(visitante, relacion);
            }
        }
        return result;
    }
}
