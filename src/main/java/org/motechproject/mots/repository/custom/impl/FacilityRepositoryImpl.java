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
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.domain.enums.FacilityType;
import org.motechproject.mots.repository.custom.FacilityRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class FacilityRepositoryImpl extends BaseRepositoryImpl
    implements FacilityRepositoryCustom {

  /**
   * Finds Facilities matching all of the provided parameters.
   * If there are no parameters, return all Facilities.
   */
  @Override
  public Page<Facility> search(String facilityId, String facilityName, String facilityType,
      String inchargeFullName, String parentChiefdom, Pageable pageable)
      throws IllegalArgumentException {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Facility> query = builder.createQuery(Facility.class);
    query = prepareQuery(query, facilityId, facilityName, facilityType,
        inchargeFullName, parentChiefdom, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, facilityId, facilityName, facilityType,
        inchargeFullName, parentChiefdom, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    Pair<Integer, Integer> maxAndFirst = getMaxAndFirstResult(pageable);
    List<Facility> incharges = entityManager.createQuery(query)
        .setMaxResults(maxAndFirst.getLeft())
        .setFirstResult(maxAndFirst.getRight())
        .getResultList();

    return new PageImpl<>(incharges, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String facilityId,
      String facilityName, String facilityType, String inchargeFullName, String parentChiefdom,
      boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Facility> root = query.from(Facility.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (facilityId != null) {
      predicate = builder.and(predicate,
          builder.like(root.get(FACILITY_ID), '%' + facilityId + '%'));
    }
    if (facilityName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + facilityName + '%'));
    }
    if (facilityType != null) {
      FacilityType validFacilityType = FacilityType.valueOf(facilityType.toUpperCase());
      predicate = builder.and(predicate,
          builder.equal(root.get(FACILITY_TYPE), validFacilityType));
    }
    if (inchargeFullName != null) {
      predicate = builder.and(predicate, builder.like(root.get(INCHARGE).get(FIRST_NAME),
          '%' + inchargeFullName + '%'));
      predicate = builder.and(predicate, builder.like(root.get(INCHARGE).get(SECOND_NAME),
          '%' + inchargeFullName + '%'));
      predicate = builder.and(predicate, builder.like(root.get(INCHARGE).get(OTHER_NAME),
          '%' + inchargeFullName + '%'));
    }
    if (parentChiefdom != null) {
      predicate = builder.and(predicate, builder.like(root.get(CHIEFDOM).get(NAME),
          '%' + parentChiefdom + '%'));
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
      if (order.getProperty().equals("parent")) {
        path = root.get(DISTRICT).get(NAME);
        Order mountedOrder = getSortDirection(builder, order, path);
        orders.add(mountedOrder);
      } else if (order.getProperty().equals("inchargeFullName")) {
        path = root.get(INCHARGE).get(FIRST_NAME);
        Order mountedOrder = getSortDirection(builder, order, path);
        orders.add(mountedOrder);
        path = root.get(INCHARGE).get(SECOND_NAME);
        mountedOrder = getSortDirection(builder, order, path);
        orders.add(mountedOrder);
        path = root.get(INCHARGE).get(OTHER_NAME);
        mountedOrder = getSortDirection(builder, order, path);
        orders.add(mountedOrder);
      } else {
        path = root.get(order.getProperty());
        Order mountedOrder = getSortDirection(builder, order, path);
        orders.add(mountedOrder);
      }
    }
    return query.orderBy(orders);
  }
}
