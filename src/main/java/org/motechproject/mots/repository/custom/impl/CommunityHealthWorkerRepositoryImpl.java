package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.CommunityHealthWorker;
import org.motechproject.mots.repository.custom.CommunityHealthWorkerRepositoryCustom;
import org.motechproject.mots.web.CommunityHealthWorkerController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
public class CommunityHealthWorkerRepositoryImpl extends BaseRepositoryImpl
    implements CommunityHealthWorkerRepositoryCustom {

  /**
   * Finds CommunityHealthWorkers matching all of the provided parameters.
   * If there are no parameters, return all CommunityHealthWorkers.
   */
  @Override
  public Page<CommunityHealthWorker> searchCommunityHealthWorkers(
      String chwId, String chwName, String phoneNumber,
      String villageName, String facilityName, String sectorName,
      String districtName, String groupName, Boolean selected, Pageable pageable) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<CommunityHealthWorker> query = builder.createQuery(CommunityHealthWorker.class);
    query = prepareQuery(query, chwId, chwName,
        phoneNumber, villageName, facilityName,
        sectorName, districtName, groupName, selected, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, chwId, chwName,
        phoneNumber, villageName, facilityName,
        sectorName, districtName, groupName, selected, true, pageable);

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
      String chwId, String chwName,
      String phoneNumber, String villageName, String facilityName,
      String sectorName, String districtName, String groupName,
      Boolean selected, boolean count, Pageable pageable) {

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
    if (chwName != null) {
      predicate = builder.and(predicate, builder.like(root.get(CHW_NAME),
          '%' + chwName.trim() + '%'));
    }
    if (phoneNumber != null) {
      predicate = builder.and(predicate, builder.like(root.get(PHONE_NUMBER),
          '%' + phoneNumber.trim() + '%'));
    }
    if (villageName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(VILLAGE).get(NAME), '%' + villageName.trim() + '%'));
    }
    if (facilityName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(FACILITY).get(NAME), '%' + facilityName.trim() + '%'));
    }
    if (sectorName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(SECTOR).get(NAME), '%' + sectorName.trim()  + '%'));
    }
    if (districtName != null) {
      predicate = builder.and(predicate, builder.like(
          root.get(DISTRICT).get(NAME), '%' + districtName.trim()  + '%'));
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
      case CommunityHealthWorkerController.VILLAGE_NAME_PARAM:
        return root.get(VILLAGE).get(NAME);
      case CommunityHealthWorkerController.FACILITY_NAME_PARAM:
        return root.get(FACILITY).get(NAME);
      case CommunityHealthWorkerController.SECTOR_NAME_PARAM:
        return root.get(SECTOR).get(NAME);
      case CommunityHealthWorkerController.DISTRICT_NAME_PARAM:
        return root.get(DISTRICT).get(NAME);
      case CommunityHealthWorkerController.GROUP_NAME_PARAM:
        return root.get(GROUP).get(NAME);
      default:
        return root.get(order.getProperty());
    }
  }
}
