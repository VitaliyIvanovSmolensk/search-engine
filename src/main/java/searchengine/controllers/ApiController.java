package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.interfaces.IndexingService;
import searchengine.services.interfaces.StatisticsService;


@RestController
@RequestMapping("/api")
public class ApiController {
    public static boolean INDEXING_CHECK = false;

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    @Autowired
    public ApiController(StatisticsService statisticsService, IndexingService indexingService) {
        this.statisticsService = statisticsService;
        this.indexingService = indexingService;
    }


    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping("/startIndexing")
    public ResponseEntity<IndexingService> indexing() {
        if (INDEXING_CHECK == true) {
            String hedder = "Индексация уже запущена";
            return new ResponseEntity(hedder,HttpStatus.OK);
        }
        indexingService.startIndexing();
        INDEXING_CHECK = true;
        return new ResponseEntity(HttpStatus.OK);

    }
}
