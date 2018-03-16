import _ from 'lodash';

export function buildSearchParams(filtered, sorted, page, pageSize) {
  const searchParams = {};

  _.forEach(filtered, (filter) => {
    searchParams[filter.id] = filter.value;
  });

  if (!_.isEmpty(sorted)) {
    const order = sorted[0].desc === true ? 'desc' : 'asc';
    searchParams.sort = `${sorted[0].id},${order}`;
  }

  searchParams.page = page;
  searchParams.size = pageSize;
  return searchParams;
}

export default buildSearchParams;
