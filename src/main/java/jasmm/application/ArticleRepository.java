package jasmm.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * Repräsentiert die Artikel Repo
 * @author Matthias
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

}
