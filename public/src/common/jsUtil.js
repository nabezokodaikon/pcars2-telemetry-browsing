export function isJson(obj) {
  return Object.keys(obj).length > 0;
}

export function isArray(array) {
  return Array.isArray(array);
}

export function existsKey(obj, key) {
  return (key in obj)
}
