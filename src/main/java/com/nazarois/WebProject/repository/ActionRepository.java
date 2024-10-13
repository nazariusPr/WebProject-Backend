package com.nazarois.WebProject.repository;

import com.nazarois.WebProject.model.Action;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, UUID> {
  @Query("SELECT a FROM Action a WHERE a.user.id = :userId")
  Page<Action> findAllByUserId(UUID userId, Pageable pageable);
}
