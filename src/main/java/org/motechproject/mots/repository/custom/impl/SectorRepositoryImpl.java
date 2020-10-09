package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.motechproject.mots.domain.Sector;
import org.motechproject.mots.repository.custom.SectorRepositoryCustom;
import org.motechproject.mots.web.LocationController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SectorRepositoryImpl extends BaseRepositoryImpl
    implements SectorRepositoryCustom {

  /**
   * Finds {@link Sector}s matching all of the provided parameters.
   * If there are no parameters, return all Sectors.
   *
   * @param pageable pagination parameters (page size, page number, sort order)
   * @param sectorName name of sector
   * @param parentDistrict name of {@link org.motechproject.mots.domain.District} that
   *        may belong to a Sector
   * @return page with found sectors
   */
  @Override
  public Page<Sector> search(String sectorName, String parentDistrict, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Sector> query = builder.createQuery(Sector.class);
    query = prepareQuery(query, sectorName, parentDistrict, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, sectorName, parentDistrict, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<Sector> sectors = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(sectors, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String sectorName,
      String parentDistrict, boolean count, Pageable pageable) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Sector> root = query.from(Sector.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (sectorName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + sectorName.trim() + '%'));
    }
    if (parentDistrict != null) {
      predicate = builder.and(predicate, builder.like(root.get(DISTRICT).get(NAME),
          '%' + parentDistrict.trim() + '%'));
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
