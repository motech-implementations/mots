export default function buildSearchParams(params) {
  let stringParams = '';
  params.forEach((param) => {
    if (param.defaultValue) {
      stringParams += `&${param.name}=${param.defaultValue}`;
    }
  });
  return stringParams;
}
