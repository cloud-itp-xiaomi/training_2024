import React from 'react';
import { connect } from 'react-redux';
import { toggleTodo, setFilter } from '../actions';
import { ActionCreators as UndoActionCreators } from 'redux-undo';
import '../App.css';

const getTodoClassName = todo => {
    if (todo.completed) {
      return 'todo-item completed';
    } else if (todo.isOverdue) {
      return 'todo-item overdue';
    } else {
      return 'todo-item pending';
    }
  };
  

  
const TodoList = ({ todos, filter, toggleTodo, setFilter, undo, redo }) => {
  const filteredTodos = todos.filter(todo => {
    if (filter === 'completed') return todo.completed;
    if (filter === 'incomplete') return !todo.completed;
    return true;
  });

  return (
    <div>
      <div className="filter-buttons">
        <button onClick={() => setFilter('all')} className={filter === 'all' ? 'active' : ''}>All</button>
        <button onClick={() => setFilter('completed')} className={filter === 'completed' ? 'active' : ''}>Completed</button>
        <button onClick={() => setFilter('incomplete')} className={filter === 'incomplete' ? 'active' : ''}>Incomplete</button>
      </div>
      <ul>
        {filteredTodos.map(todo => (
          <li
            key={todo.id}
            onClick={() => toggleTodo(todo.id)}
            className={getTodoClassName(todo)}
          >
            {todo.text}
          </li>
        ))}
      </ul>
      <div className="undo-redo">
        <button onClick={undo}>Undo</button>
        <button onClick={redo}>Redo</button>
      </div>
    </div>
  );
};

const mapStateToProps = state => ({
  todos: state.todos.present,
  filter: state.filter,
});

const mapDispatchToProps = dispatch => ({
  toggleTodo: id => dispatch(toggleTodo(id)),
  setFilter: filter => dispatch(setFilter(filter)),
  undo: () => dispatch(UndoActionCreators.undo()),
  redo: () => dispatch(UndoActionCreators.redo()),
});

export default connect(mapStateToProps, mapDispatchToProps)(TodoList);
