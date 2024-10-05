package com.nazarois.WebProject.model;

import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "actions")
@Entity
public class Action {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "action_type")
  private ActionType actionType;

  @Enumerated(EnumType.STRING)
  @Column(name = "action_status")
  private ActionStatus actionStatus;

  @OneToMany(mappedBy = "action")
  private List<Image> images;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @PrePersist
  private void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
