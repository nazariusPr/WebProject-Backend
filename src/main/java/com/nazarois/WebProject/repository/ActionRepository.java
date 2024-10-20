package com.nazarois.WebProject.repository;

import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.enums.ActionStatus;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository
    extends JpaRepository<Action, UUID>, JpaSpecificationExecutor<Action> {
  @Query("SELECT a FROM Action a WHERE a.user.email = :email")
  Page<Action> findAllByUserEmail(String email, Pageable pageable);

  @Query(
      "SELECT COUNT(a) FROM Action a WHERE a.user.email = :email AND a.actionStatus = :actionStatus")
  int countActionsOfStatus(String email, ActionStatus actionStatus);
}
