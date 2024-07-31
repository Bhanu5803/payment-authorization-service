package com.bridgeline.authorize.repository;

import com.bridgeline.authorize.domain.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTypeRepository extends JpaRepository<CardType,Integer> {
    CardType findByCardId(Integer cardId);
}
