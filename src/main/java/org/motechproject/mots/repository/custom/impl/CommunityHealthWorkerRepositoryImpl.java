package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
      String chwId, String firstName, String secondName, String otherName, String phoneNumber,
      String educationLevel, String communityName, String facilityName, String chiefdomName,
      String districtName, String phuSupervisor, String groupName, Boolean selected,
      Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<CommunityHealthWorker> query = builder.createQuery(CommunityHealthWorker.class);
    query = prepareQuery(query, chwId, firstName, secondName,
        otherName, phoneNumber, educationLevel, communityName, facilityName,
        chiefdomName, districtName, phuSupervisor, groupName, selected, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, chwId, firstName, secondName,
        otherName, phoneNumber, educationLevel, communityName, facilityName,
        chiefdomName, districtName, phuSupervisor, groupName, selected, true, pageable);

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
      String phoneNumber, String educationLevel, String communityName, String facilityName,
      String chiefdomName, String districtName, String phuSupervisor, String groupName,
      Boolean selected, boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<CommunityHealthWorker> root = query.from(CommunityHealthWorker.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (chwId != null) {
      predicate = builder.and(predicate, builder.like(root.get(CHW_ID), '%' + chwId.trim() + '%'));
    }
    if (firstName != null) {
      predicate = builder.and(predicate, builder.like(root.get(FIRST_NAME),
          '%' + firstName.trim() + '%'));
    }
    if (secondName != null) {
      predicate = builder.and(predicate, builder.like(root.get(SECOND_NAME),
          '%' + secondName.trim() + '%'));
    }
    if (otherName != null) {
      predicate = builder.and(predicate, builder.like(root.get(OTHER_NAME),
          '%' + otherName.trim() + '%'));
    }
    if (phoneNumber != null) {
      predicate = builder.and(predicate, builder.like(root.get(PHONE_NUMBER),
          '%' + phoneNumber.trim() + '%'));
    }
    if (educationLevel != null) {
      EducationLevel validLevel = EducationLevel.valueOf(educationLevel.trim().toUpperCase());
      predicate = builder.and(predicate, builder.equal(root.get(EDUCATION_LEVEL), validLevel));
    }
    if (communityName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(NAME), '%' + communityName.trim() + '%'));
    }
    if (facilityName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(NAME), '%' + facilityName.trim() + '%'));
    }
    if (chiefdomName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(NAME),
          '%' + chiefdomName.trim()  + '%'));
    }
    if (districtName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(DISTRICT).get(NAME),
          '%' + districtName.trim()  + '%'));
    }
    if (phuSupervisor != null) {
      Predicate localPredicate = builder.like(
          root.get(COMMUNITY).get(FACILITY).get(INCHARGE).get(FIRST_NAME),
          '%' + phuSupervisor.trim() + '%');
      localPredicate = builder.or(localPredicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(INCHARGE).get(SECOND_NAME),
          '%' + phuSupervisor.trim() + '%'));
      localPredicate = builder.or(localPredicate, builder.like(
          root.get(COMMUNITY).get(FACILITY).get(INCHARGE).get(OTHER_NAME),
          '%' + phuSupervisor.trim() + '%'));

      predicate = builder.and(predicate, localPredicate);
    }
    if (groupName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(GROUP).get(NAME), '%' + groupName.trim() + '%'));
    }

    if (selected != null) {
      predicate = builder.and(predicate, builder.equal(
          root.get(SELECTED), selected));
    }

    query.where(predicate);

    if (!count && pageable != null && pageable.getSort() != null) {
      query = addSortProperties(query, root, pageable);
    }

    return query;
  }

  @Override
  protected Path getPath(Root root, Sort.Order order) {
    switch (order.getProperty()) {
      case CommunityHealthWorkerController.COMMUNITY_NAME_PARAM:
        return root.get(COMMUNITY).get(NAME);
      case CommunityHealthWorkerController.FACILITY_NAME_PARAM:
        return root.get(COMMUNITY).get(FACILITY).get(NAME);
      case CommunityHealthWorkerController.CHIEFDOM_NAME_PARAM:
        return root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(NAME);
      case CommunityHealthWorkerController.DISTRICT_NAME_PARAM:
        return root.get(COMMUNITY).get(FACILITY).get(CHIEFDOM).get(DISTRICT).get(NAME);
      case CommunityHealthWorkerController.PHU_SUPERVISOR_PARAM:
        return root.get(COMMUNITY).get(FACILITY).get(INCHARGE).get(FIRST_NAME);
      case CommunityHealthWorkerController.GROUP_NAME_PARAM:
        return root.get(GROUP).get(NAME);
      default:
        return root.get(order.getProperty());
    }
  }
}
