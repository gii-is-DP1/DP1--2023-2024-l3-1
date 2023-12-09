
/**
 * Crea un formulario con etiquetas para cada elemento y con el espaciado correcto entre
 * la descripcion y el campo a rellenar.
 *
 * @param {*} map - Objeto con la forma: `{ descripcion: <Elemento> }`
 */
export function useLabeledForm(map) {
    return (
        <table>
            <thead>
                <tr>
                    <th className="text-center" />
                    <th className="text-center" />
                </tr>
            </thead>
            <tbody>
                {Object.entries(map).map(([key, value]) => {
                    return (
                        <tr key={key}>
                            <td className="text-center">
                                {key}
                            </td>
                            <td className="text-center">
                                {value}
                            </td>
                        </tr>
                    );
                })}
            </tbody>
        </table>
    );
}
