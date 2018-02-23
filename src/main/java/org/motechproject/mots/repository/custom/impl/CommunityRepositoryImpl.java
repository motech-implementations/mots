package org.motechproject.mots.repository.custom.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.tuple.Pair;
import org.motechproject.mots.domain.Community;
import org.motechproject.mots.repository.custom.CommunityRepositoryCustom;
import org.motechproject.mots.web.LocationController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CommunityRepositoryImpl extends BaseRepositoryImpl implements
    CommunityRepositoryCustom {

  /**
   * Finds Communities matching all of the provided parameters.
   * If there are no parameters, return all Communities.
   */
  @Override
  public Page<Community> search(String communityName, String parentFacility, Pageable pageable)
      throws IllegalArgumentException {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Community> query = builder.createQuery(Community.class);
    query = prepareQuery(query, communityName, parentFacility, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, communityName, parentFacility, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<Community> incharges = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(incharges, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String communityName,
      String parentFacility, boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Community> root = query.from(Community.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (communityName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + communityName + '%'));
    }
    if (parentFacility != null) {
      predicate = builder.and(predicate, builder.like(root.get(FACILITY).get(NAME),
          '%' + parentFacility + '%'));
    }

    query.where(predicate);

    if (!count && pageable != null && pageable.getSort() != null) {
      query = addSortProperties(query, root, pageable);
    }

    return query;
  }

  @Override
  protected <T> CriteriaQuery<T> addSortProperties(CriteriaQuery<T> query,
      Root root, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    List<Order> orders = new ArrayList<>();
    Iterator<Sort.Order> iterator = pageable.getSort().iterator();

    Sort.Order order;
    Path<T> path;
    while (iterator.hasNext()) {
      order = iterator.next();
      if (order.getProperty().equals(LocationController.PARENT_PARAM)) {
        path = root.get(FACILITY).get(NAME);
      } else {
        path = root.get(order.getProperty());
      }

      Order mountedOrder = getSortDirection(builder, order, path);
      orders.add(mountedOrder);
    }
    return query.orderBy(orders);
  }
}
