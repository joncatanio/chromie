package com.catanio.chromie.dm.repositories;

import com.catanio.chromie.dm.entities.Karma;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KarmaRepository extends CrudRepository<Karma, Long> {
    /*
     * Using custom HQL for some of these queries, further documentation on the
     * implicit JOIN syntax can be found in the reference docs at:
     * http://docs.jboss.org/hibernate/core/3.6/reference/en-US/html_single/#queryhql-joins
     */
    @Query("SELECT SUM(points)" +
            " FROM Karma AS K" +
            " WHERE K.recipient.slackId = ?1")
    Long sumKarmaForSlackId(String slackId);
}
