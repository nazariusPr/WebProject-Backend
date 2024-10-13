package com.nazarois.WebProject.model;

import com.nazarois.WebProject.annotation.ValidSize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "action_requests")
@Entity
public class ActionRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(name = "prompt")
  private String prompt;

  @ValidSize
  @Column(name = "size")
  private String size;

  @Column(name = "image_number")
  private int numImages;

  @OneToOne
  @JoinColumn(name = "action_id", unique = true, nullable = false)
  private Action action;
}
