package org.motechproject.mots.repository.custom.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.Incharge;
import org.motechproject.mots.repository.custom.InchargeRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SuppressWarnings("PMD.CyclomaticComplexity")
public class InchargeRepositoryImpl implements InchargeRepositoryCustom {

  private static final String FIRST_NAME = "firstName";
  private static final String SECOND_NAME = "secondName";
  private static final String OTHER_NAME = "otherName";
  private static final String PHONE_NUMBER = "phoneNumber";
  private static final String EMAIL = "email";
  private static final String NAME = "name";
  private static final String FACILITY = "facility";

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Finds Incharges matching all of the provided parameters. If there are no parameters, return all
   * Incharges.
   */
  @Override
  public Page<Incharge> searchIncharges(String firstName, String secondName,
      String otherName, String phoneNumber, String email, String facilityName, Pageable pageable)
      throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Incharge> query = builder.createQuery(Incharge.class);
    query = prepareQuery(query, firstName, secondName, otherName, phoneNumber, email, facilityName,
        false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, firstName, secondName, otherName, phoneNumber, email,
        facilityName, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = null != pageable ? pageable.getPageSize() : 0;
    int firstResult = null != pageable ? pageable.getPageNumber() * pageSize : 0;

    List<Incharge> incharges = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(incharges, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query,
      String firstName, String secondName, String otherName, String phoneNumber, String email,
      String facilityName, boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Incharge> root = query.from(Incharge.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (firstName != null) {
      predicate = builder.and(predicate, builder.like(root.get(FIRST_NAME),
          '%' + firstName + '%'));
    }
    if (secondName != null) {
      predicate = builder.and(predicate, builder.like(root.get(SECOND_NAME),
          '%' + secondName + '%'));
    }
    if (otherName != null) {
      predicate = builder.and(predicate, builder.like(root.get(OTHER_NAME),
          '%' + otherName + '%'));
    }
    if (phoneNumber != null) {
      predicate = builder.and(predicate, builder.like(root.get(PHONE_NUMBER),
          '%' + phoneNumber + '%'));
    }
    if (email != null) {
      predicate = builder.and(predicate, builder.like(root.get(EMAIL),
          '%' + email + '%'));
    }
    if (facilityName != null) {
      predicate = builder.and(predicate, builder.like(root.get(FACILITY).get(NAME),
          '%' + facilityName + '%'));
    }

    query.where(predicate);

    if (!count && pageable != null && pageable.getSort() != null) {
      query = addSortProperties(query, root, pageable);
    }

    return query;
  }

  private <T> CriteriaQuery<T> addSortProperties(CriteriaQuery<T> query,
      Root root, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    List<Order> orders = new ArrayList<>();
    Iterator<Sort.Order> iterator = pageable.getSort().iterator();

    Sort.Order order;
    Path<T> path;
    while (iterator.hasNext()) {
      order = iterator.next();
      if (order.getProperty().equals("facilityName")) {
        path = root.get(FACILITY).get(NAME);
      } else {
        path = root.get(order.getProperty());
      }

      Order mountedOrder = getDirection(builder, order, path);
      orders.add(mountedOrder);
    }
    return query.orderBy(orders);
  }

  private <T> Order getDirection(CriteriaBuilder builder, Sort.Order order, Path<T> path) {
    Order mountedOrder;
    if (order.isAscending()) {
      mountedOrder = builder.asc(path);
    } else {
      mountedOrder = builder.desc(path);
    }
    return mountedOrder;
  }
}
