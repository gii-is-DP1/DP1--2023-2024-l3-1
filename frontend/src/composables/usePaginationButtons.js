import DButton from "../components/ui/DButton";

/**
 * Para páginas que deben de mostrar muchos datos, muestralos botones para cambiar de página con estilos convenientes
 *
 * @param {*} setCurrentPage - Función para cambiar la página actual de la lista
 * @param {*} currentPage - Página actual de la lista
 * @param {*} array - Array de elementos a mostrar
 */
export function usePaginationButtons(setCurrentPage, currentPage, array) {
    return (
        <div style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center' }}>
            <DButton color="red" onClick={() => {
                if (currentPage > 0) {
                    setCurrentPage(prevPage => prevPage - 1)
                }
            }} disabled={array.length === 1 || currentPage === 0}>
                Página anterior
            </DButton>
            {currentPage + 1} / {array.length}
            <DButton color="green" onClick={() => {
                if (currentPage < array.length - 1) {
                    setCurrentPage(prevPage => prevPage + 1)
                }
            }} disabled={array.length === 1 || currentPage === array.length - 1}>
                Página siguiente
            </DButton>
        </div>
    );
}
