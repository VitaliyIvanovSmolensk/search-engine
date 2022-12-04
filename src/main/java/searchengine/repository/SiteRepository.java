package searchengine.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import searchengine.model.Site;

import java.util.Optional;
@EnableJpaRepositories
public interface SiteRepository extends JpaRepository<Site, Integer> {

    Optional<Site> findByUrl(String path);
}
