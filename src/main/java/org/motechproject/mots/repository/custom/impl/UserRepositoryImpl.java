package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.repository.custom.UserRepositoryCustom;
import org.motechproject.mots.web.UserController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class UserRepositoryImpl extends BaseRepositoryImpl
    implements UserRepositoryCustom {

  /**
   * Finds Users matching all of the provided parameters.
   * If there are no parameters, return all Users.
   */
  @Override
  public Page<User> search(String username, String email, String name, String role,
      Pageable pageable) throws IllegalArgumentException {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<User> query = builder.createQuery(User.class);
    query = prepareQuery(query, username, email, name, role, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, username, email, name, role, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<User> users = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(users, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String username, String email,
      String name, String role, boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<User> root = query.from(User.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (username != null) {
      predicate = builder.and(predicate, builder.like(root.get(USERNAME),
          '%' + username.trim() + '%'));
    }
    if (email != null) {
      predicate = builder.and(predicate, builder.like(root.get(EMAIL),
          '%' + email.trim() + '%'));
    }
    if (name != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + name.trim() + '%'));
    }
    if (role != null) {
      Join<User, UserRole> roleJoin = root.join(ROLES);
      predicate = builder.and(predicate, builder.like(roleJoin.get(NAME),
          '%' + role.trim() + '%'));
    }

    query.where(predicate);

    if (!count && pageable != null && pageable.getSort() != null) {
      query = addSortProperties(query, root, pageable);
    }

    return query;
  }

  @Override
  protected Path getPath(Root root, Sort.Order order) {
    Path path;
    if (order.getProperty().equals(UserController.ROLE_PARAM)) {
      Join<User, UserRole> roleJoin = root.join(ROLES);
      path = roleJoin.get(NAME);
    } else {
      path = root.get(order.getProperty());
    }
    return path;
  }
}
