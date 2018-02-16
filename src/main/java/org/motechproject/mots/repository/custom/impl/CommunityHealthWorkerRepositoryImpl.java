package org.motechproject.mots.repository.custom.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.repository.custom.CommunityHealthWorkerRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SuppressWarnings("PMD.CyclomaticComplexity")
public class CommunityHealthWorkerRepositoryImpl implements CommunityHealthWorkerRepositoryCustom {

  private static final String CHW_ID = "chwId";
  private static final String FIRST_NAME = "firstName";
  private static final String SECOND_NAME = "secondName";
  private static final String OTHER_NAME = "otherName";
  private static final String PHONE_NUMBER = "phoneNumber";
  private static final String EDUCATION_LEVEL = "educationLevel";
  private static final String ID = "id";
  private static final String FACILITY = "facility";
  private static final String CHIEFDOM = "chiefdom";
  private static final String DISTRICT = "district";
  private static final String COMMUNITY = "community";

  @PersistenceContext
  private EntityManager entityManager;

  /**
   * Finds CommunityHealthWorkers matching all of the provided parameters.
   * If there are no parameters, return all CommunityHealthWorkers.
   */
  @Override
  public Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String firstName, String secondName, String otherName,
      String phoneNumber, String educationLevel, String communityId, String facilityId,
      String chiefdomId, String districtId, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<CommunityHealthWorker> query = builder.createQuery(CommunityHealthWorker.class);
    query = prepareQuery(query, chwId, firstName, secondName,
        otherName, phoneNumber, educationLevel, communityId,
        facilityId, chiefdomId, districtId, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, chwId, firstName, secondName,
        otherName, phoneNumber, educationLevel, communityId,
        facilityId, chiefdomId, districtId, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = null != pageable ? pageable.getPageSize() : 0;
    int firstResult = null != pageable ? pageable.getPageNumber() * pageSize : 0;

    List<CommunityHealthWorker> communityHealthWorkers = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(communityHealthWorkers, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query,
      String chwId, String firstName, String secondName, String otherName,
      String phoneNumber, String educationLevel, String communityId,
      String facilityId, String chiefdomId, String districtId,
      boolean count,
      Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<CommunityHealthWorker> root = query.from(CommunityHealthWorker.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (chwId != null) {
      predicate = builder.and(predicate, builder.equal(root.get(CHW_ID), chwId));
    }
    if (firstName != null) {
      predicate = builder.and(predicate, builder.equal(root.get(FIRST_NAME), firstName));
    }
    if (secondName != null) {
      predicate = builder.and(predicate, builder.equal(root.get(SECOND_NAME), secondName));
    }
    if (otherName != null) {
      predicate = builder.and(predicate, builder.equal(root.get(OTHER_NAME), otherName));
    }
    if (phoneNumber != null) {
      predicate = builder.and(predicate, builder.equal(root.get(PHONE_NUMBER), phoneNumber));
    }
    if (educationLevel != null) {
      EducationLevel validLevel = EducationLevel.valueOf(educationLevel.toUpperCase());
      predicate = builder.and(predicate, builder.equal(root.get(EDUCATION_LEVEL), validLevel));
    }
    if (communityId != null) {
      predicate = builder.and(predicate, builder.equal(
          root.get(COMMUNITY).get(ID), UUID.fromString(communityId)));
    }
    if (facilityId != null) {
      predicate = builder.and(predicate, builder.equal(
          root.get(COMMUNITY).get(FACILITY).get(ID), UUID.fromString(facilityId)));
    }
    if (chiefdomId != null) {
      predicate = builder.and(predicate, builder.equal(
          root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(ID), UUID.fromString(chiefdomId)));
    }
    if (districtId != null) {
      predicate = builder.and(predicate, builder.equal(
          root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(DISTRICT).get(ID),
          UUID.fromString(districtId)));
    }

    query.where(predicate);
    query.distinct(true);

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
    while (iterator.hasNext()) {
      order = iterator.next();
      if (order.isAscending()) {
        orders.add(builder.asc(root.get(order.getProperty())));
      } else {
        orders.add(builder.desc(root.get(order.getProperty())));
      }
    }
    return query.orderBy(orders);
  }
}
