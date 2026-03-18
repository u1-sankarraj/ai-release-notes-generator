package com.releasenotes.ai_release_notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.releasenotes.ai_release_notes.entity.ReleaseNotesEntity;

@Repository
public interface ReleaseNotesRepository extends JpaRepository<ReleaseNotesEntity, Long> {
	ReleaseNotesEntity findTopByOrderByIdDesc();
}
