package searchengine.model.mappers;

import org.springframework.stereotype.Service;
import searchengine.model.Site;
import searchengine.model.enums.Status;

import java.util.Date;

@Service
public class MapperSite {

    public static Site map (String url, String name, int codeStatus){
        Site site = new Site();
        site.setUrl(url);
        site.setName(name);
        switch (codeStatus){
            case 0 -> site.setStatus(Status.INDEXING);
            case 1 -> site.setStatus(Status.INDEXED);
            case -1 -> site.setStatus(Status.FAILED);
        }
        site.setLastError("none");
        site.setStatusTime(new Date());
        return site;
    }
}
