package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.Chiefdom;
import org.motechproject.mots.repository.custom.ChiefdomRepositoryCustom;
import org.motechproject.mots.web.LocationController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ChiefdomRepositoryImpl extends BaseRepositoryImpl
    implements ChiefdomRepositoryCustom {

  /**
   * Finds Chiefdoms matching all of the provided parameters.
   * If there are no parameters, return all Chiefdoms.
   */
  @Override
  public Page<Chiefdom> search(String chiefdomName, String parentDistrict, Pageable pageable)
      throws IllegalArgumentException {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Chiefdom> query = builder.createQuery(Chiefdom.class);
    query = prepareQuery(query, chiefdomName, parentDistrict, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, chiefdomName, parentDistrict, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<Chiefdom> incharges = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(incharges, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String chiefdomName,
      String parentDistrict, boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Chiefdom> root = query.from(Chiefdom.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (chiefdomName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + chiefdomName + '%'));
    }
    if (parentDistrict != null) {
      predicate = builder.and(predicate, builder.like(root.get(DISTRICT).get(NAME),
          '%' + parentDistrict + '%'));
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
    if (order.getProperty().equals(LocationController.PARENT_PARAM)) {
      path = root.get(DISTRICT).get(NAME);
    } else {
      path = root.get(order.getProperty());
    }
    return path;
  }
}
