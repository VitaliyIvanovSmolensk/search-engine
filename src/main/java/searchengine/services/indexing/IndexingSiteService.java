package searchengine.services.indexing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.controllers.ApiController;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.model.enums.Status;
import searchengine.model.mappers.MapperPage;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.indexing.index_node.mapers.MapperIndexingNode;


import java.util.concurrent.ForkJoinPool;

@Service
public class IndexingSiteService implements Runnable {

    public final Site site;
    public SiteRepository siteRepository;
    public PageRepository pageRepository;


    @Autowired
    public IndexingSiteService(Site site, SiteRepository siteRepository, PageRepository pageRepository) {
        this.site = site;
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;

    }

    @Override
    public void run() {
        SearchPageFromSite nodeRoot = new SearchPageFromSite(MapperIndexingNode.map(site), siteRepository, pageRepository);
        Page page = MapperPage.map(site, site.getUrl());
        pageRepository.save(page);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(nodeRoot);
        ApiController.INDEXING_CHECK = false;
        site.setStatus(Status.INDEXED);
        siteRepository.save(site);
    }


}

