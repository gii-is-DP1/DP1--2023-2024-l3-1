import { useState } from "react";
import useFetchState from "../util/useFetchState";
import getIdFromUrl from "../util/getIdFromUrl";
import tokenService from "../services/token.service";
import useFetchData from "../util/useFetchData";

const jwt = tokenService.getLocalAccessToken();

export default function ProfileEdit() {
    const emptyItem = {
        id: null,
        username: "",
        password: "",
        icon: "",
        authority: null,
      };
    const id = getIdFromUrl(2);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [dobbleUser, setDobbleUser] = useFetchState(
        emptyItem,
        `/api/v1/dobbleUsers/${id}`,
        jwt,
        setMessage,
        setVisible,
        id
    )
    const auths = useFetchData(`/api/v1/users/authorities`, jwt);





}
