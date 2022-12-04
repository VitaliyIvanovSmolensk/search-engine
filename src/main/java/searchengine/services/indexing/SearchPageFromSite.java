package searchengine.services.indexing;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.Page;
import searchengine.model.mappers.MapperPage;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.services.indexing.index_node.Node;


import java.io.IOException;

import java.util.*;
import java.util.concurrent.RecursiveTask;

@Service
public class SearchPageFromSite extends RecursiveTask<Boolean> {
    public static Set  pageSet = Collections.synchronizedSet(new HashSet<String>());


    private Node node;
    public PageRepository pageRepository;
    public SiteRepository siteRepository;

    @Autowired
    public SearchPageFromSite(Node node, SiteRepository siteRepository, PageRepository pageRepository) {
        this.node = node;
        this.pageRepository = pageRepository;
        this.siteRepository = siteRepository;
    }

    @Override
    protected Boolean compute() {
        List<SearchPageFromSite> taskList = new ArrayList<>();
        pageSet.add(node.getPath());
        try {
            Elements elements = readSite(node.getPath());
            for (Element element : elements) {
                if (element.absUrl("href").contains(node.getPath()) &&
                    !element.absUrl("href").contains("#")) {
                    String line = element.absUrl("href");
                    Page page = MapperPage.map(node.getSiteId(), line);
                    Node child = new Node(page.getSite(), page.getPath(), page.getCode(), page.getContent());
                    if (!pageSet.contains(child.getPath())) {
                        pageRepository.save(page);
                        SearchPageFromSite task = new SearchPageFromSite(child, siteRepository, pageRepository);
                        task.fork();
                        taskList.add(task);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        taskList.forEach(task -> task.join());
        return true;
    }

    private synchronized Elements readSite(String link) throws IOException, InterruptedException {
        Document doc = Jsoup.connect(node.getPath()).ignoreHttpErrors(true).followRedirects(true).ignoreContentType(true).get();
        Elements elements = doc.select("a");
        return elements;
    }

}
