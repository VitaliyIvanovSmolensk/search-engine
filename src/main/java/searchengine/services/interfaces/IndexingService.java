package searchengine.services.interfaces;

import searchengine.model.Page;
import searchengine.model.Site;

import java.util.Optional;

public interface IndexingService {

     void startIndexing();

     void deleteSite();

     void saveSite(Site site);

     void deletePage();

     void savePage(Page page);

     int getSiteId(String path);

     Optional<Site> getSiteById (int id);
}
