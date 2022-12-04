package searchengine.model.mappers;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import searchengine.model.Page;
import searchengine.model.Site;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class MapperPage {

    public static Page map (Site site, String url) {
        Page page = new Page();
        try {
            page.setSite(site);
            page.setPath(url);
            page.setCode(getCode(url));
            page.setContent(Jsoup.connect(url).get().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return page;
    }

    public static int getCode(String url) throws IOException {
            URL urlComnect = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlComnect.openConnection();
            int statusCode =  http.getResponseCode();
        return statusCode;
    }
}
