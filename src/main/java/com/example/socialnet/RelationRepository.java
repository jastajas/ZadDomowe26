package com.example.socialnet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Long> {

    @Query("SELECT relation FROM Relation relation WHERE relation.user_initial = :user AND relation.confirmed = true")
    List <Relation> listAllByUser_initialAndConfirmedIsTrue(@Param("user") User user);

    @Query("SELECT relation FROM Relation relation WHERE relation.user_invited = :usera AND relation.confirmed = true")
    List <Relation> listAllByUser_invitedAndConfirmedIsTrue(@Param("usera") User usera);

}
