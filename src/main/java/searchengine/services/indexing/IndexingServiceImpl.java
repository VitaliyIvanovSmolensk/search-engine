package searchengine.services.indexing;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.SiteFromList;
import searchengine.config.SitesList;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.mappers.MapperSite;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.interfaces.IndexingService;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < sitesList.size(); i++) {
            Site site = MapperSite.map(sitesList.get(i).getUrl(), sitesList.get(i).getName(), 0);
            saveSite(site);
            IndexingSiteService indexingSiteService = new IndexingSiteService(site, siteRepository, pageRepository);
            threads.add(new Thread(indexingSiteService));
        }
        threads.forEach(thread -> thread.start());
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






