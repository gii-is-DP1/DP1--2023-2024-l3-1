import DButton from "../ui/DButton";

export default function NumberOfPlayers(props) {
  function handleIncrement(e) {
    e.preventDefault();
    if (props.game.max_players < 8) {
      props.setGame(prevGame => ({ ...prevGame, max_players: prevGame.max_players + 1 }));
    }
  }

  function handleDecrement(e) {
    e.preventDefault();
    if (props.game.max_players > 3) {
      props.setGame(prevGame => ({ ...prevGame, max_players: prevGame.max_players - 1 }));
    }
  }

  return <>
    <DButton color="red" onClick={handleDecrement} disabled={props.game.max_players === 3}>-</DButton>
      {props.game.max_players} / 8
    <DButton color="green" onClick={handleIncrement} disabled={props.game.max_players === 8}>+</DButton>
  </>;
}
