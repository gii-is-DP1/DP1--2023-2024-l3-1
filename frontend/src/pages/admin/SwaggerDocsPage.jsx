import { useState, useEffect } from "react";
import axios from '../../services/api';
import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css"

export default function SwaggerDocsPage(){
    const [spec, setSpec] = useState({});

    useEffect(() =>{
        (async () => {
            const response = await axios.get("/docs");
            setSpec(response.data);
        })();
    }, []);

    return spec ? <SwaggerUI spec={spec} /> : (<></>);    
}
