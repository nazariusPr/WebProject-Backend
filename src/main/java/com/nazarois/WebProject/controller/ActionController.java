package com.nazarois.WebProject.controller;

import static com.nazarois.WebProject.constants.AppConstants.ACTION_LINK;

import com.nazarois.WebProject.dto.action.ActionDto;
import com.nazarois.WebProject.dto.action.ActionFilterDto;
import com.nazarois.WebProject.dto.action.ActionRequestDto;
import com.nazarois.WebProject.dto.action.DetailActionDto;
import com.nazarois.WebProject.dto.page.PageDto;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.service.ActionService;
import com.nazarois.WebProject.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ValidationException;
import java.security.Principal;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(ACTION_LINK)
public class ActionController {
  private final ActionService actionService;
  private final SseService sseService;

  @Operation(summary = "Read all actions of the user")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Actions retrieved successfully")
  })
  @GetMapping
  public ResponseEntity<PageDto<ActionDto>> read(Pageable pageable, Principal principal) {
    log.info("**/ Reading all actions of user");
    return ResponseEntity.ok(actionService.read(principal.getName(), pageable));
  }

  @Operation(summary = "Filter actions of the user")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Filtered actions retrieved successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied")
  })
  @GetMapping("/filter")
  public ResponseEntity<PageDto<ActionDto>> read(
      ActionFilterDto filter, Pageable pageable, Principal principal) {
    log.info("**/ Filtering all actions of user");
    return ResponseEntity.ok(actionService.read(filter, principal.getName(), pageable));
  }

  @Operation(summary = "Read a specific action by ID")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Action retrieved successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied"),
    @ApiResponse(responseCode = "404", description = "Action not found")
  })
  @GetMapping("/{actionId}")
  @PreAuthorize("@securityUtils.hasAccess(#actionId, authentication) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<DetailActionDto> read(@PathVariable UUID actionId) {
    log.info("**/ Reading action of user");
    return ResponseEntity.ok(actionService.read(actionId));
  }

  @Operation(summary = "Generate an image based on action request")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Image generated successfully"),
    @ApiResponse(responseCode = "400", description = "Invalid request data"),
    @ApiResponse(responseCode = "401", description = "Unauthorized"),
  })
  @PostMapping("/generate-image")
  public ResponseEntity<ActionDto> generateImage(
      @Validated @RequestBody ActionRequestDto request, BindingResult result, Principal principal) {
    if (result.hasErrors()) {
      log.error("**/ Bad request to generate image");
      throw new ValidationException(
          Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
    }
    log.info("**/ Generating image");
    return ResponseEntity.ok(actionService.generate(request, principal.getName()));
  }

  @Operation(summary = "Subscribe to updates for a specific action")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Subscribed to updates successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied")
  })
  @GetMapping("/{actionId}/updates")
  @PreAuthorize("@securityUtils.hasAccess(#actionId, #accessToken) or hasRole('ROLE_ADMIN')")
  public SseEmitter subscribeToActionUpdates(
      @PathVariable UUID actionId, @RequestParam String accessToken) {
    log.info("**/ Subscribing to action updates " + actionId);
    return sseService.registerClient(actionId);
  }

  @Operation(summary = "Get the current status of a specific action")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied"),
    @ApiResponse(responseCode = "404", description = "Action not found")
  })
  @GetMapping("/{actionId}/status")
  @PreAuthorize("@securityUtils.hasAccess(#actionId, authentication) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<ActionStatus> getActionStatus(@PathVariable UUID actionId) {
    log.info("**/ Getting status of action " + actionId);
    return ResponseEntity.ok(actionService.readActionStatus(actionId));
  }

  @Operation(summary = "Restart an action")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Action restarted successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied"),
    @ApiResponse(responseCode = "404", description = "Action not found")
  })
  @PatchMapping("/restart/{actionId}")
  @PreAuthorize("@securityUtils.hasAccess(#actionId, authentication) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<ActionDto> restartAction(@PathVariable UUID actionId, Principal principal) {
    log.info("**/ Restarting action");
    return ResponseEntity.ok(actionService.restart(actionId, principal.getName()));
  }

  @Operation(summary = "Cancel an action")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Action canceled successfully"),
    @ApiResponse(responseCode = "403", description = "Access denied"),
    @ApiResponse(responseCode = "404", description = "Action not found")
  })
  @PatchMapping("/cancel/{actionId}")
  @PreAuthorize("@securityUtils.hasAccess(#actionId, authentication) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<ActionDto> cancelAction(@PathVariable UUID actionId) {
    log.info("**/ Cancelling action");
    return ResponseEntity.ok(actionService.cancel(actionId));
  }
}
