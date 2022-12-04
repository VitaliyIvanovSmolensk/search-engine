package searchengine.services.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.SiteFromList;
import searchengine.config.SitesList;
import searchengine.controllers.ApiController;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.enums.Status;
import searchengine.model.mappers.MapperSite;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.indexing.index_node.mapers.MapperIndexingNode;
import searchengine.services.interfaces.IndexingService;


import java.util.*;
import java.util.concurrent.*;

@Service
public class IndexingServiceImpl implements IndexingService {


    public final SitesList sites;
    public final SiteRepository siteRepository;
    public final PageRepository pageRepository;

    @Autowired
    public IndexingServiceImpl(SitesList sites, SiteRepository siteRepository, PageRepository pageRepository) {
        this.sites = sites;
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }


    @Override
    public void startIndexing() {
        List<SiteFromList> sitesList = sites.getSites();
        ExecutorService service = Executors.newCachedThreadPool();
        ForkJoinPool pool = new ForkJoinPool();
        Set<Callable<Boolean>> sitesFromIndexing = new HashSet<>();
        for (int i = 0; i < sitesList.size(); i++) {
            Site site = MapperSite.map(sitesList.get(i).getUrl(), sitesList.get(i).getName(), 0);
            saveSite(site);
            sitesFromIndexing.add(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    SearchPageFromSite nodeRoot = new SearchPageFromSite(MapperIndexingNode.map(site), siteRepository, pageRepository);
                    boolean result = pool.invoke(nodeRoot);
                    if (result == true) {
                        ApiController.INDEXING_CHECK = false;
                        site.setStatus(Status.INDEXED);
                        siteRepository.save(site);
                    }
               return true;
                }
            });
            try {
                service.invokeAll(sitesFromIndexing);
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public void deleteSite() {
        siteRepository.deleteAll();
    }

    @Override
    public void saveSite(Site site) {
        siteRepository.save(site);
    }

    @Override
    public void deletePage() {
        pageRepository.deleteAll();
    }

    @Override
    public void savePage(Page page) {
        pageRepository.save(page);
    }

    @Override
    public int getSiteId(String path) {
        Optional<Site> siteOpt = siteRepository.findByUrl(path);
        if (siteOpt.isPresent()) {
            Site site = siteOpt.get();
            return site.getId();
        }
        return -1;
    }

    @Override
    public Optional<Site> getSiteById(int id) {
        return siteRepository.findById(id);
    }

}






