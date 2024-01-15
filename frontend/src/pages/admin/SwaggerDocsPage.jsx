import { useEffect, useState } from "react";
import SwaggerUI from "swagger-ui-react";
import "swagger-ui-react/swagger-ui.css";
import axios from '../../services/api';

export default function SwaggerDocsPage(){
    const [spec, setSpec] = useState({});

    useEffect(() =>{
        (async () => {
            const response = await axios.get("/docs");
            setSpec(response.data);
        })();
    }, []);

    return spec ?
    <div style={{ backgroundColor: 'white', display: 'flex', flexDirection: 'column' }}>
        <SwaggerUI spec={spec} />
    </div>
    : (<></>);    
}
