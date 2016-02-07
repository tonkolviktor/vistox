package hu.tonkol.data.vistox.common;

import java.util.Map;
import java.util.Optional;

public class MapUtils {


    @SuppressWarnings("unchecked")
    public static <T> T getOrThrowException(Optional<Map<String, ?>> data, String key) {
        if(!data.isPresent()) {
            throw new BusinessException("Cannot access key: " + key + ". Map is not present");
        }
        if(!data.get().containsKey(key) || data.get().get(key) == null) {
            throw new BusinessException("Cannot access key: " + key + ". Key is not present or value is null");
        }

        return (T) data.get().get(key);
    }
}
