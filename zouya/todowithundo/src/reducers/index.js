import { combineReducers } from 'redux';
import todos from './todos';
import filter from './filter';
import undoable from 'redux-undo';

const rootReducer = combineReducers({
  todos: undoable(todos),
  filter,
});

export default rootReducer;
