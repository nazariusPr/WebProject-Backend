package com.nazarois.WebProject.repository;

import com.nazarois.WebProject.model.ActionRequest;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRequestRepository extends JpaRepository<ActionRequest, UUID> {}
