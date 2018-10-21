package com.catanio.chromie.repositories;

import com.catanio.chromie.entities.Karma;
import com.catanio.chromie.entities.KarmaFromDonorSum;
import com.catanio.chromie.entities.TotalUserKarma;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Query("SELECT new com.catanio.chromie.entities.TotalUserKarma(K.recipient, SUM(K.points))" +
           " FROM Karma AS K" +
           " GROUP BY K.recipient")
    List<TotalUserKarma> sumKarmaForUsers();

    @Query("SELECT new com.catanio.chromie.entities.KarmaFromDonorSum(K.recipient, K.donor, SUM(K.points))" +
           " FROM Karma AS K" +
           " WHERE K.recipient.slackId = ?1" +
           " GROUP BY K.recipient, K.donor")
    List<KarmaFromDonorSum> sumKarmaFromDonorsFor(String slackId);
}
