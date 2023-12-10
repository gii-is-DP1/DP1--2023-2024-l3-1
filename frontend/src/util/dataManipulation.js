export function dividirArray(array, piezas) {
  const subarrays = [];

  while (array.length > 0) {
    subarrays.push(array.splice(0, piezas));
  }

  if (subarrays.length === 0) {
    subarrays.push([]);
  }

  return subarrays;
}
