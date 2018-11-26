const doFilter = (item, filter) => {
  let { value } = filter;

  if (!(value instanceof RegExp)) {
    value = new RegExp(value, 'i');
  }

  return value.test(item[filter.property]);
};

const createFilter = (...filters) => item => filters.every(filter => doFilter(item, filter));

export default createFilter;
