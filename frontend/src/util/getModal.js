import { Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
import DButton from "../components/ui/DButton";

function handleVisible(setVisible, visible) {
    setVisible(!visible);
}

/**
 * 
 * @param {*} setMessage - Cambia la visibilidad del elemento. Si hay mensaje se muestra la alerta, si no se oculta
 * @param {*} message - Cambia el cuerpo de la alerta
 * @param {*} header - Establece la cabecera de la alerta
 * @param {*} actions - Establece los botones de la alerta
 * @returns 
 */
export default function getModal(setMessage, message = undefined, header = 'Error', actions = undefined) {
    if (message) {
        const closeBtn = (
            <DButton onClick={() => handleVisible(setMessage, message)} type="button">X</DButton>
        );
        return (
            <div>
                <Modal centered isOpen={Boolean(message)} style={{ color: 'white' }} toggle={() => handleVisible(setMessage, message)}
                    keyboard={false}>
                    {header ? <ModalHeader toggle={() => handleVisible(setMessage, message)} close={closeBtn}>{header}</ModalHeader> : <></>}
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
