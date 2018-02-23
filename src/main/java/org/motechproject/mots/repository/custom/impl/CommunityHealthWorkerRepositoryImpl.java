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
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.domain.enums.EducationLevel;
import org.motechproject.mots.repository.custom.CommunityHealthWorkerRepositoryCustom;
import org.motechproject.mots.web.CommunityHealthWorkerController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SuppressWarnings("PMD.CyclomaticComplexity")
public class CommunityHealthWorkerRepositoryImpl extends BaseRepositoryImpl
    implements CommunityHealthWorkerRepositoryCustom {

  /**
   * Finds CommunityHealthWorkers matching all of the provided parameters.
   * If there are no parameters, return all CommunityHealthWorkers.
   */
  @Override
  public Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String firstName, String secondName, String otherName,
      String phoneNumber, String educationLevel, String communityName, String facilityName,
      String chiefdomName, String districtName, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<CommunityHealthWorker> query = builder.createQuery(CommunityHealthWorker.class);
    query = prepareQuery(query, chwId, firstName, secondName,
        otherName, phoneNumber, educationLevel, communityName,
        facilityName, chiefdomName, districtName, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, chwId, firstName, secondName,
        otherName, phoneNumber, educationLevel, communityName,
        facilityName, chiefdomName, districtName, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<CommunityHealthWorker> communityHealthWorkers = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(communityHealthWorkers, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query,
      String chwId, String firstName, String secondName, String otherName,
      String phoneNumber, String educationLevel, String communityName,
      String facilityName, String chiefdomName, String districtName,
      boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<CommunityHealthWorker> root = query.from(CommunityHealthWorker.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (chwId != null) {
      predicate = builder.and(predicate, builder.like(root.get(CHW_ID), '%' + chwId + '%'));
    }
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
    if (educationLevel != null) {
      EducationLevel validLevel = EducationLevel.valueOf(educationLevel.toUpperCase());
      predicate = builder.and(predicate, builder.equal(root.get(EDUCATION_LEVEL), validLevel));
    }
    if (communityName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(NAME), '%' + communityName + '%'));
    }
    if (facilityName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(NAME), '%' + facilityName + '%'));
    }
    if (chiefdomName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(NAME),
          '%' + chiefdomName  + '%'));
    }
    if (districtName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(DISTRICT).get(NAME),
          '%' + districtName  + '%'));
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
      if (order.getProperty().equals(CommunityHealthWorkerController.COMMUNITY_NAME_PARAM)) {
        path = root.get(COMMUNITY).get(NAME);
      } else {
        path = root.get(order.getProperty());
      }

      Order mountedOrder = getSortDirection(builder, order, path);
      orders.add(mountedOrder);
    }
    return query.orderBy(orders);
  }
}
