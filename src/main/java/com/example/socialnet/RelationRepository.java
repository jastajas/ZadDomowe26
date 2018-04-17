package com.example.socialnet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelationRepository extends JpaRepository<Relation, Long> {

    @Query("SELECT relation FROM Relation relation WHERE relation.user_initial = :user AND relation.confirmed = true")
    List <Relation> listAllByUser_initialAndConfirmedIsTrue(@Param("user") User user);

    @Query("SELECT relation FROM Relation relation WHERE relation.user_invited = :usera AND relation.confirmed = true")
    List <Relation> listAllByUser_invitedAndConfirmedIsTrue(@Param("usera") User usera);

    @Query("SELECT relation FROM Relation relation WHERE relation.user_invited = :usera AND relation.confirmed = false")
    List <Relation> listAllByUser_invitedAndConfirmedIsFalse(@Param("usera") User usera);

    @Query("SELECT relation FROM Relation relation WHERE relation.user_initial = :user AND relation.confirmed = false")
    List <Relation> listAllByUser_initialAndConfirmedIsFalse(@Param("user") User user);

    @Query("SELECT relation FROM Relation relation WHERE (relation.user_initial = :user OR relation.user_initial = :usera)" +
            " AND (relation.user_invited = :user OR relation.user_invited = :usera)")
    List <Relation> checkSet(@Param("user") User user, @Param("usera") User usera);

    @Query("SELECT relation FROM Relation relation WHERE relation.user_initial = :user " +
            " AND relation.user_invited = :usera AND relation.confirmed = false")
    Optional<Relation> findRelationByUserAndUser(@Param("user") User user, @Param("usera") User usera);
}
