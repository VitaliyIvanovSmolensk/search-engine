package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;

import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {

    Optional<Object> findByPath(String path);
}
