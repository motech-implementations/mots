package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.security.UserRole;
import org.motechproject.mots.repository.custom.RoleRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryImpl extends BaseRepositoryImpl implements RoleRepositoryCustom {

  /**
   * Finds Roles matching all of the provided parameters.
   * If there are no parameters, return all Roles.
   */
  @Override
  public Page<UserRole> search(String name, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<UserRole> query = builder.createQuery(UserRole.class);
    query = prepareQuery(query, name, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, name, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<UserRole> roles = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(roles, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String name,
      boolean count, Pageable pageable) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<UserRole> root = query.from(UserRole.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (name != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + name.trim() + '%'));
    }

    query.where(predicate);

    if (!count && pageable != null && pageable.getSort() != null) {
      query = addSortProperties(query, root, pageable);
    }

    return query;
  }
}
