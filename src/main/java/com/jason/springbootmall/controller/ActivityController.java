package com.jason.springbootmall.controller;

import com.jason.springbootmall.dto.ActivityQueryParams;
import com.jason.springbootmall.dto.CreateActivityRequest;
import com.jason.springbootmall.model.Activity;
import com.jason.springbootmall.model.ActivityParticipant;
import com.jason.springbootmall.service.ActivityService;
import com.jason.springbootmall.service.UserService;
import com.jason.springbootmall.util.Page;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activities")
public class ActivityController {

  @Autowired private ActivityService activityService;
  @Autowired private UserService userService;

  @GetMapping
  public ResponseEntity<Page<Activity>> getActivities(
      @RequestParam(required = false) String skillLevel,
      @RequestParam(required = false) String status,
      @RequestParam(defaultValue = "20") Integer limit,
      @RequestParam(defaultValue = "0") Integer offset) {

    ActivityQueryParams params = new ActivityQueryParams();
    if (skillLevel != null) {
      try {
        params.setSkillLevel(com.jason.springbootmall.constant.SkillLevel.valueOf(skillLevel));
      } catch (Exception ignored) {
      }
    }
    if (status != null) {
      try {
        params.setStatus(com.jason.springbootmall.constant.ActivityStatus.valueOf(status));
      } catch (Exception ignored) {
      }
    }
    params.setLimit(limit);
    params.setOffset(offset);

    return ResponseEntity.ok(activityService.getActivities(params));
  }

  @GetMapping("/{activityId}")
  public ResponseEntity<Activity> getActivity(@PathVariable Integer activityId) {
    return ResponseEntity.ok(activityService.getActivityById(activityId));
  }

  @PostMapping
  public ResponseEntity<Activity> createActivity(
      @RequestBody @Valid CreateActivityRequest request, Authentication authentication) {
    Integer userId = getUserId(authentication);
    Integer activityId = activityService.createActivity(userId, request);
    Activity activity = activityService.getActivityById(activityId);
    return ResponseEntity.status(HttpStatus.CREATED).body(activity);
  }

  @DeleteMapping("/{activityId}")
  public ResponseEntity<Void> cancelActivity(
      @PathVariable Integer activityId, Authentication authentication) {
    Integer userId = getUserId(authentication);
    boolean isAdmin = isAdmin(authentication);
    activityService.cancelActivity(activityId, userId, isAdmin);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{activityId}/join")
  public ResponseEntity<Void> joinActivity(
      @PathVariable Integer activityId, Authentication authentication) {
    Integer userId = getUserId(authentication);
    activityService.joinActivity(activityId, userId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{activityId}/leave")
  public ResponseEntity<Void> leaveActivity(
      @PathVariable Integer activityId, Authentication authentication) {
    Integer userId = getUserId(authentication);
    activityService.leaveActivity(activityId, userId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{activityId}/participants")
  public ResponseEntity<List<ActivityParticipant>> getParticipants(
      @PathVariable Integer activityId, Authentication authentication) {
    Integer userId = getUserId(authentication);
    boolean isAdmin = isAdmin(authentication);
    return ResponseEntity.ok(activityService.getParticipants(activityId, userId, isAdmin));
  }

  @GetMapping("/my/hosted")
  public ResponseEntity<List<Activity>> getHostedActivities(Authentication authentication) {
    Integer userId = getUserId(authentication);
    return ResponseEntity.ok(activityService.getHostedActivities(userId));
  }

  @GetMapping("/my/joined")
  public ResponseEntity<List<ActivityParticipant>> getJoinedActivities(
      Authentication authentication) {
    Integer userId = getUserId(authentication);
    return ResponseEntity.ok(activityService.getJoinedActivities(userId));
  }

  private Integer getUserId(Authentication authentication) {
    String email = authentication.getName();
    return userService.getUserByEmail(email).getUserId();
  }

  private boolean isAdmin(Authentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch("ROLE_ADMIN"::equals);
  }
}
