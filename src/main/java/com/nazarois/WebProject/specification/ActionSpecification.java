package com.nazarois.WebProject.specification;

import com.nazarois.WebProject.dto.action.ActionFilterDto;
import com.nazarois.WebProject.model.Action;
import com.nazarois.WebProject.model.enums.ActionStatus;
import com.nazarois.WebProject.model.enums.ActionType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@RequiredArgsConstructor
public class ActionSpecification implements Specification<Action> {
  private final transient ActionFilterDto filter;

  @Override
  public Predicate toPredicate(
      Root<Action> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    query.distinct(true);
    query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
    List<Predicate> predicates = createPredicates(root, criteriaBuilder);

    return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
  }

  private List<Predicate> createPredicates(Root<Action> root, CriteriaBuilder cb) {
    return null;
  }
  private Predicate createPromptPredicate(Root<Action> root, CriteriaBuilder cb, String prompt){
    return cb.like(cb.lower(root.get("prompt")), "%" + prompt.toLowerCase() + "%");
  }
  private Predicate createActionTypePredicate(
      Root<Action> root, CriteriaBuilder cb, ActionType actionType) {
    return cb.equal(root.get("actionType"), actionType);
  }

  private Predicate createActionStatusPredicate(
      Root<Action> root, CriteriaBuilder cb, ActionStatus actionStatus) {
    return cb.equal(root.get("actionStatus"), actionStatus);
  }
}
