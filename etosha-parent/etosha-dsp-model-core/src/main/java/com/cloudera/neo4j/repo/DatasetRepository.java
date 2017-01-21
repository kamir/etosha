package com.cloudera.neo4j.repo;

import com.cloudera.neo4j.domain.DataSet;
import java.util.Collection;
import java.util.List;
import java.util.Map; 
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param; 
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

 
 
@RepositoryRestResource(collectionResourceRel = "dataset", path = "dataset")
public interface DatasetRepository extends GraphRepository<DataSet> {
    
    DataSet findByTitle(@Param("title") String title);

    @Query("MATCH (m:Movie) WHERE m.title =~ ('(?i).*'+{title}+'.*') RETURN m")
    Collection<DataSet> findByTitleContaining(@Param("title") String title);

    @Query("MATCH (m:Movie)<-[:ACTED_IN]-(a:Person) RETURN m.title as movie, collect(a.name) as cast LIMIT {limit}")
    List<Map<String,Object>> graph(@Param("limit") int limit);

}
