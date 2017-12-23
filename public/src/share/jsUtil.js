export function isJson(obj) {
  return obj !== null 
    && obj !== undefined 
    && typeof obj !== "string"
    && Object.keys(obj).length > 0;
}

export function isArray(array) {
  return Array.isArray(array);
}

export function existsKey(obj, key) {
  return (key in obj)
}

export function sleep(time) {
  return new Promise((resolve) => setTimeout(resolve, time));
}
