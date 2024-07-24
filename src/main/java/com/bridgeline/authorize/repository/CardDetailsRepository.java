package com.bridgeline.authorize.repository;

import com.bridgeline.authorize.domain.CardDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDetailsRepository extends JpaRepository<CardDetails,Integer> {
}
