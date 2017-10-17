export function isJson(obj) {
  return Object.keys(obj).length > 0;
}

export function existsKey(obj, key) {
  return (key in obj)
}
