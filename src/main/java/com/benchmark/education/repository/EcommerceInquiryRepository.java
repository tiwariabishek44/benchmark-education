package com.benchmark.education.repository;

import com.benchmark.education.entity.EcomerceEnquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcommerceInquiryRepository extends JpaRepository<EcomerceEnquiry, Integer> {
}
