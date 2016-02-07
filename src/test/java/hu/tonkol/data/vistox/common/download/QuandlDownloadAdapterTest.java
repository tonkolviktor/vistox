package hu.tonkol.data.vistox.common.download;

import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class QuandlDownloadAdapterTest {

    private QuandlDownloadAdapter quandlDownloadAdapter = new QuandlDownloadAdapter();

    @Test
    public void shouldDownloadFile() {
        //given
        //when
        Path path = quandlDownloadAdapter.download("MULTPL/SP500_REAL_PRICE_MONTH.csv", Optional.<Map<String, ?>>empty());
        //then
        assertThat(path).exists();
    }
}
