import { useDispatch } from "react-redux";
import { ActionCreators } from "redux-undo";
import classes from "./TodoUndoRedo.module.css";

function TodoUndoRedo() {
  const dispatch = useDispatch();
  return (
    <div className={classes.undo}>
      <button
        className={classes.button}
        onClick={() => dispatch(ActionCreators.undo())}
      >
        撤销
      </button>
      <button
        className={classes.button}
        onClick={() => dispatch(ActionCreators.redo())}
      >
        恢复
      </button>
    </div>
  );
}
export default TodoUndoRedo;
