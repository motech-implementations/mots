package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.Village;
import org.motechproject.mots.repository.custom.VillageRepositoryCustom;
import org.motechproject.mots.web.LocationController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class VillageRepositoryImpl extends BaseRepositoryImpl implements
    VillageRepositoryCustom {

  /**
   * Finds Villages matching all of the provided parameters.
   * If there are no parameters, return all Villages.
   */
  @Override
  public Page<Village> search(String villageName, String parentFacility, String sectorName,
      String districtName, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Village> query = builder.createQuery(Village.class);
    query = prepareQuery(query, villageName, parentFacility, sectorName, districtName,
        false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, villageName, parentFacility, sectorName, districtName,
        true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<Village> villages = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(villages, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String villageName,
      String parentFacility, String sectorName, String districtName,
      boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Village> root = query.from(Village.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (villageName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + villageName.trim() + '%'));
    }
    if (parentFacility != null) {
      predicate = builder.and(predicate, builder.like(root.get(FACILITY).get(NAME),
          '%' + parentFacility.trim() + '%'));
    }
    if (sectorName != null) {
      predicate = builder.and(
          predicate, builder.like(root.get(FACILITY).get(SECTOR).get(NAME),
          '%' + sectorName.trim() + '%')
      );
    }
    if (districtName != null) {
      predicate = builder.and(
          predicate, builder.like(root.get(FACILITY).get(SECTOR).get(DISTRICT).get(NAME),
          '%' + districtName.trim() + '%')
      );
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
      path = root.get(FACILITY).get(NAME);
    } else if (order.getProperty().equals(LocationController.SECTOR_NAME_PARAM)) {
      path = root.get(FACILITY).get(SECTOR).get(NAME);
    } else if (order.getProperty().equals(LocationController.DISTRICT_NAME_PARAM)) {
      path = root.get(FACILITY).get(SECTOR).get(DISTRICT).get(NAME);
    } else {
      path = root.get(order.getProperty());
    }
    return path;
  }
}
