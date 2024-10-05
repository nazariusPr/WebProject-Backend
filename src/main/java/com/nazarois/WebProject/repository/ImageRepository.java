package com.nazarois.WebProject.repository;

import com.nazarois.WebProject.model.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {}
