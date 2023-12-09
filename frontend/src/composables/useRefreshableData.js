import { useState, useEffect } from "react";

/**
 * Refresca los datos y devuelve un contador con el tiempo restante para la siguiente actualización
 * 
 * @param {*} refreshFunction - Función a ejecutar para refrescar los datos
 * @param {*} interval - Intervalo en el que se refrescarán los datos (en segundos)
 */
export function useRefreshableData(refreshFunction, interval) {
    const [refreshCounter, setRefreshCounter] = useState(interval);

    async function performRefresh() {
        await refreshFunction();
        setRefreshCounter(interval);
    }

    useEffect(() => {
        const timer = setInterval(() => {
            if (refreshCounter > 0) {
                setRefreshCounter(prevRefreshCounter => prevRefreshCounter - 1);
            }
        }, 1000);

        return () => {
            clearInterval(timer);
        };
    }, []);

    useEffect(() => {
        if (refreshCounter === 0) {
            performRefresh();
        }
    }, [refreshCounter]);

    return (
        <p>
            {refreshCounter > 0 ?
                <>
                    Próxima actualización en {refreshCounter} {refreshCounter === 1 ? <>segundo</> : <>segundos</>}
                </>
                :
                <>
                    Actualizando...
                </>
            }
        </p>
    );
}
