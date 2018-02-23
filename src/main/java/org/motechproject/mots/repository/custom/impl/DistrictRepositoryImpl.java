package org.motechproject.mots.repository.custom.impl;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.tuple.Pair;
import org.motechproject.mots.domain.District;
import org.motechproject.mots.repository.custom.DistrictRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class DistrictRepositoryImpl extends BaseRepositoryImpl
    implements DistrictRepositoryCustom {

  /**
   * Finds Districts matching all of the provided parameters.
   * If there are no parameters, return all Districts.
   */
  @Override
  public Page<District> search(String districtName, Pageable pageable)
      throws IllegalArgumentException {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<District> query = builder.createQuery(District.class);
    query = prepareQuery(query, districtName, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, districtName, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<District> incharges = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(incharges, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query, String districtName,
      boolean count, Pageable pageable) throws IllegalArgumentException {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<District> root = query.from(District.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();
    if (districtName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + districtName + '%'));
    }

    query.where(predicate);

    if (!count && pageable != null && pageable.getSort() != null) {
      query = addSortProperties(query, root, pageable);
    }

    return query;
  }
}
