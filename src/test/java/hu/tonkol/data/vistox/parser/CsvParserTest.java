package hu.tonkol.data.vistox.parser;

import hu.tonkol.data.vistox.common.BusinessException;
import org.supercsv.cellprocessor.ParseBigDecimal;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.fail;

public class CsvParserTest {

    public static final String CSV_PATH = "csv/sp500_monthly.csv";
    private CsvParser csvParser = new CsvParser();

    @Test(expectedExceptions = BusinessException.class)
    public void shouldRequireCellProcessor() throws URISyntaxException {
        //given
        Path path = Paths.get(ClassLoader.getSystemResource(CSV_PATH).toURI());
        Consumer<Map<String, Object>> dummyConsumer = objectMap -> {};
        //when
        csvParser.parse(path, dummyConsumer, Optional.empty());
        //then
        fail("exception expected");
    }

    @Test
    public void shouldParseCsv() throws URISyntaxException {
        //given
        Path path = Paths.get(ClassLoader.getSystemResource(CSV_PATH).toURI());
        AssertConsumer assertConsumer = new AssertConsumer();
        Map<String, Object> data = new HashMap<>();
        data.put("cellprocessor", new CellProcessor[]{
                new ParseDate("yyyy-MM-dd"),//date
                new ParseBigDecimal(),//value
        });
        //when
        csvParser.parse(path, assertConsumer, Optional.of(data));
        //then
        assertThat(assertConsumer.getInvocations()).isEqualTo(1742);
    }

    private class AssertConsumer implements Consumer<Map<String, Object>> {
        private int invocations = 0;

        @Override
        public void accept(Map<String, Object> stringObjectMap) {
            invocations++;
            assertThat(stringObjectMap.size()).isEqualTo(2);
        }

        @Override
        public Consumer<Map<String, Object>> andThen(Consumer<? super Map<String, Object>> after) {
            return null;
        }

        public int getInvocations() {
            return invocations;
        }
    }
}
