package hu.tonkol.data.vistox.parser;


import hu.tonkol.data.vistox.common.BusinessException;
import hu.tonkol.data.vistox.common.MapUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class CsvParser {

    private static final String CELL_PROCESSOR_KEY = "cellprocessor";

    public void parse(Path path, Consumer<Map<String, Object>> consumer, Optional<Map<String, ?>> data) {
        CellProcessor[] cellProcessor = MapUtils.getOrThrowException(data, CELL_PROCESSOR_KEY);
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CsvMapReader mapReader = new CsvMapReader(reader, CsvPreference.STANDARD_PREFERENCE)) {

            final String[] header = mapReader.getHeader(true);

            Map<String, Object> objectMap;
            while ((objectMap = mapReader.read(header, cellProcessor)) != null) {
                consumer.accept(objectMap);
            }
        } catch (IOException e) {
            throw new BusinessException("Cannot parse CSV file: " + e.getMessage(), e);
        }
    }
}
