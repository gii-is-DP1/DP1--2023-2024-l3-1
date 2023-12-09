import { Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
import DButton from "../components/ui/DButton";

function close(setVisible) {
    setVisible(undefined);
}

/**
 * Muestra una ventana que se puede personalizar al gusto del usuario
 *
 * @param {*} setMessage - Cambia la visibilidad del elemento. Si hay mensaje se muestra la alerta, si no se oculta
 * @param {*} message - Cambia el cuerpo de la alerta
 * @param {*} header - Establece la cabecera de la alerta
 * @param {*} actions - Establece los botones de la alerta
 */
export function useModal(setMessage, message = undefined, header = 'Error', actions = undefined) {
    if (message) {
        const closeBtn = (
            <DButton onClick={() => close(setMessage)} type="button">X</DButton>
        );
        return (
            <div>
                <Modal centered isOpen={Boolean(message)} style={{ color: 'white' }} toggle={() => close(setMessage)}
                    keyboard={false}>
                    {header ? <ModalHeader close={closeBtn}>{header}</ModalHeader> : <></>}
                    <ModalBody>
                        {message}
                    </ModalBody>
                    {actions ? <ModalFooter>
                        {actions}
                    </ModalFooter> : undefined}
                </Modal>
            </div>
        )
    } else
        return <></>;
}
