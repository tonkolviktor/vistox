package hu.tonkol.data.vistox.common.download;

import hu.tonkol.data.vistox.common.BusinessException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class QuandlDownloadAdapter {

    private static final String BASE_URL = "https://www.quandl.com/api/v3/datasets/";
    private static final String API_KEY = "quandl.apikey";



    public Path download(String code, Optional<Map<String, ?>> data) {
        try {
            return doDownload(code, data);
        } catch (IOException e) {
            throw new BusinessException("Cannot download: " + code, e);
        }
    }

    private Path doDownload(String code, Optional<Map<String, ?>> data) throws IOException {
        String url = BASE_URL + code;
        url += getApiKey(data);
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        File tempFile = getTempFile();
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        return Paths.get(tempFile.toURI());
    }

    private String getApiKey(Optional<Map<String, ?>> data) {
        String key;
        if(data.isPresent() && data.get().containsKey(API_KEY)) {
            key = (String) data.get().get(API_KEY);
        } else {
            key = System.getProperty(API_KEY);
        }

        if(key == null) {
            key = "";
        }
        return key;
    }

    private File getTempFile() throws IOException {
        return File.createTempFile("quandl-download-", ".tmp");
    }
}
