package searchengine.services.indexing.index_node.mapers;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import searchengine.model.Site;
import searchengine.model.mappers.MapperPage;
import searchengine.services.indexing.index_node.Node;

import java.io.IOException;
@Service
public class MapperIndexingNode {

    public static Node map(Site site) {
        Node node = new Node();
        try {
            node.setSiteId(site);
            node.setCode(MapperPage.getCode(site.getUrl()));
            node.setPath(site.getUrl());
            node.setContent(Jsoup.connect(site.getUrl()).get().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return node;
    }
}
