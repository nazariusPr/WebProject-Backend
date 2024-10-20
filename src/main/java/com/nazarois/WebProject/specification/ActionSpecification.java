package com.nazarois.WebProject.specification;

import com.nazarois.WebProject.dto.action.ActionFilterDto;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class ActionSpecification implements Specification<Action> {
  private final transient ActionFilterDto filter;

  private final String userEmail;

  @Override
  public Predicate toPredicate(
      Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    query.distinct(true);
    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
    List<Predicate> predicates = createPredicates(root, criteriaBuilder);

    return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
  }

  private List<Predicate> createPredicates(Root<Action> root, CriteriaBuilder cb) {
    List<Predicate> predicates = new ArrayList<>();

    if (userEmail != null && !userEmail.isEmpty()) {
      predicates.add(createUserEmailPredicate(root, cb, userEmail));
    }

    if (filter.getPrompt() != null && !filter.getPrompt().isEmpty()) {
      predicates.add(createPromptPredicate(root, cb, filter.getPrompt()));
    }

    if (filter.getActionType() != null) {
      predicates.add(createActionTypePredicate(root, cb, filter.getActionType()));
    }

    if (filter.getActionStatus() != null) {
      predicates.add(createActionStatusPredicate(root, cb, filter.getActionStatus()));
    }

    if (filter.getBegin() != null) {
      predicates.add(createBeginPredicate(root, cb, filter.getBegin()));
    }

    if (filter.getEnd() != null) {
      predicates.add(createEndPredicate(root, cb, filter.getEnd()));
    }

    return predicates;
  }

  private Predicate createUserEmailPredicate(Root<Action> root, CriteriaBuilder cb, String prompt) {
    return cb.equal(root.get("user").get("email"), userEmail);
  }

  private Predicate createPromptPredicate(Root<Action> root, CriteriaBuilder cb, String prompt) {
    return cb.like(cb.lower(root.get("title")), "%" + prompt.toLowerCase() + "%");
  }

  private Predicate createActionTypePredicate(
      Root<Action> root, CriteriaBuilder cb, ActionType actionType) {
    return cb.equal(root.get("actionType"), actionType);
  }

  private Predicate createActionStatusPredicate(
      Root<Action> root, CriteriaBuilder cb, ActionStatus actionStatus) {
    return cb.equal(root.get("actionStatus"), actionStatus);
  }

  private Predicate createBeginPredicate(Root<Action> root, CriteriaBuilder cb, LocalDate begin) {
    LocalDateTime beginDateTime = begin.atStartOfDay();
    return cb.greaterThanOrEqualTo(root.get("createdAt"), beginDateTime);
  }

  private Predicate createEndPredicate(Root<Action> root, CriteriaBuilder cb, LocalDate end) {
    LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
    return cb.lessThanOrEqualTo(root.get("createdAt"), endDateTime);
  }
}
