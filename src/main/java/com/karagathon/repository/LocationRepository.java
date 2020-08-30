package com.karagathon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.karagathon.model.Location;
import com.karagathon.model.Media;
import com.karagathon.model.Report;
import com.karagathon.model.Violation;
import com.karagathon.model.Violator;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	
	public Optional<Location> findByReport( Report report );
}
