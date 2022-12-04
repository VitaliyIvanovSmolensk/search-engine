package searchengine.services.indexing.index_node;

import lombok.*;
import org.springframework.stereotype.Component;
import searchengine.model.Site;

import java.util.Comparator;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Node implements Comparator<Node> {

    private Site siteId;
    private String path;
    private int code;
    private String content;

    @Override
    public int compare(Node o1, Node o2) {
        return o1.getPath().compareTo(o2.getPath());
    }
}


