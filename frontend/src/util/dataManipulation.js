export function dividirArray(array, piezas) {
  const subarrays = [];

  while (array.length > 0) {
    subarrays.push(array.splice(0, piezas));
  }

  return subarrays;
}
