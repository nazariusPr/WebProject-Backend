package com.nazarois.WebProject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_credentials")
@Entity
public class UserCredential {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Pattern(
      regexp = "[A-Za-z\\d]{6,}",
      message = "Must be minimum 6 symbols long, using digits and latin letters")
  @Column(name = "password", nullable = false)
  private String password;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true, nullable = false)
  private User user;
}
