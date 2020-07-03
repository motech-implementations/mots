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
import org.motechproject.mots.domain.Facility;
import org.motechproject.mots.repository.custom.FacilityRepositoryCustom;
import org.motechproject.mots.web.LocationController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
public class FacilityRepositoryImpl extends BaseRepositoryImpl
    implements FacilityRepositoryCustom {

  /**
   * Finds Facilities matching all of the provided parameters.
   * If there are no parameters, return all Facilities.
   */
  @Override
  public Page<Facility> search(String facilityName,
      String inchargeFullName, String inchargePhone, String inchargeEmail,
      String parentSector, String districtName, Pageable pageable) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Facility> query = builder.createQuery(Facility.class);
    query = prepareQuery(query, facilityName, inchargeFullName,
        inchargePhone, inchargeEmail, parentSector, districtName, false, pageable);

    CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

    countQuery = prepareQuery(countQuery, facilityName, inchargeFullName,
        inchargePhone, inchargeEmail, parentSector, districtName, true, pageable);

    Long count = entityManager.createQuery(countQuery).getSingleResult();

    int pageSize = getPageSize(pageable);
    int firstResult = getFirstResult(pageable, pageSize);
    List<Facility> facilities = entityManager.createQuery(query)
        .setMaxResults(pageSize)
        .setFirstResult(firstResult)
        .getResultList();

    return new PageImpl<>(facilities, pageable, count);
  }

  private <T> CriteriaQuery<T> prepareQuery(CriteriaQuery<T> query,
      String facilityName, String inchargeFullName, String inchargePhone,
      String inchargeEmail, String parentSector, String districtName,
      boolean count, Pageable pageable) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    Root<Facility> root = query.from(Facility.class);

    if (count) {
      CriteriaQuery<Long> countQuery = (CriteriaQuery<Long>) query;
      query = (CriteriaQuery<T>) countQuery.select(builder.count(root));
    }

    Predicate predicate = builder.conjunction();

    if (facilityName != null) {
      predicate = builder.and(predicate, builder.like(root.get(NAME),
          '%' + facilityName.trim() + '%'));
    }
    if (inchargeFullName != null) {
      predicate = builder.and(predicate,
          builder.like(root.get(INCHARGE_FULL_NAME), '%' + inchargeFullName.trim() + '%'));
    }
    if (inchargePhone != null) {
      predicate = builder.and(predicate,
          builder.like(root.get(INCHARGE_PHONE), '%' + inchargePhone.trim() + '%'));
    }
    if (inchargeEmail != null) {
      predicate = builder.and(predicate,
          builder.like(root.get(INCHARGE_EMAIL), '%' + inchargeEmail.trim() + '%'));
    }
    if (parentSector != null) {
      predicate = builder.and(predicate, builder.like(root.get(SECTOR).get(NAME),
          '%' + parentSector.trim() + '%'));
    }
    if (districtName != null) {
      predicate = builder.and(
          predicate, builder.like(root.get(SECTOR).get(DISTRICT).get(NAME),
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
        path = root.get(SECTOR).get(NAME);
        Order mountedOrder = getSortDirection(builder, order, path);
        orders.add(mountedOrder);
      } else if (order.getProperty().equals(LocationController.DISTRICT_NAME_PARAM)) {
        path = root.get(SECTOR).get(DISTRICT).get(NAME);
        Order mountedOrder = getSortDirection(builder, order, path);
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
