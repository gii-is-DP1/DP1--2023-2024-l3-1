import "../../static/css/lobby/playButton.css";

function PlayButton(){

    function handleClick(){
        return null;
    }

    return( 
        <button
        className='play-button'  
        onClick={handleClick}>Play! </button>
    );
}


export default function MainLobby(){
    return (
        <div>
        <h1> Main Lobby</h1>
        <PlayButton/>
        </div>
    );
}