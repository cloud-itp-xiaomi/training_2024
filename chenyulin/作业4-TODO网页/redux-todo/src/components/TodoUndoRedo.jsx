import { useDispatch } from "react-redux";
import { ActionCreators } from "redux-undo";

function TodoUndoRedo() {
  const dispatch = useDispatch();
  return (
    <>
      <button onClick={() => dispatch(ActionCreators.undo())}>撤销</button>
      <button onClick={() => dispatch(ActionCreators.redo())}>恢复</button>
    </>
  );
}
export default TodoUndoRedo;
