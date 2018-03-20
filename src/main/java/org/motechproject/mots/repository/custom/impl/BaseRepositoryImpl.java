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
import javax.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class BaseRepositoryImpl {

  protected static final String CHW_ID = "chwId";
  protected static final String FACILITY_ID = "facilityId";
  protected static final String FIRST_NAME = "firstName";
  protected static final String SECOND_NAME = "secondName";
  protected static final String OTHER_NAME = "otherName";
  protected static final String PHONE_NUMBER = "phoneNumber";
  protected static final String EDUCATION_LEVEL = "educationLevel";
  protected static final String FACILITY_TYPE = "type";
  protected static final String NAME = "name";
  protected static final String FACILITY = "facility";
  protected static final String CHIEFDOM = "chiefdom";
  protected static final String DISTRICT = "district";
  protected static final String COMMUNITY = "community";
  protected static final String INCHARGE = "incharge";
  protected static final String EMAIL = "email";
  protected static final String USERNAME = "username";
  protected static final String ROLES = "roles";
  protected static final String SELECTED = "selected";

  @PersistenceContext
  protected EntityManager entityManager;

  protected int getFirstResult(Pageable pageable, int pageSize) {
    return null != pageable ? pageable.getPageNumber() * pageSize : 0;
  }

  protected int getPageSize(Pageable pageable) {
    return null != pageable ? pageable.getPageSize() : 0;
  }

  protected <T> CriteriaQuery<T> addSortProperties(CriteriaQuery<T> query,
      Root root, Pageable pageable) {
    CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    List<Order> orders = new ArrayList<>();
    Iterator<Sort.Order> iterator = pageable.getSort().iterator();

    Sort.Order order;
    Path<T> path;
    while (iterator.hasNext()) {
      order = iterator.next();
      path = getPath(root, order);

      Order mountedOrder = getSortDirection(builder, order, path);
      orders.add(mountedOrder);
    }
    return query.orderBy(orders);
  }

  protected Path getPath(Root root, Sort.Order order) {
    return root.get(order.getProperty());
  }

  protected static <T> Order getSortDirection(CriteriaBuilder builder, Sort.Order order,
      Path<T> path) {
    Order mountedOrder;
    if (order.isAscending()) {
      mountedOrder = builder.asc(path);
    } else {
      mountedOrder = builder.desc(path);
    }
    return mountedOrder;
  }
}
