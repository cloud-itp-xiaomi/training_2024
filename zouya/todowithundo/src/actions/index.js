export const ADD_TODO = 'ADD_TODO';
export const TOGGLE_TODO = 'TOGGLE_TODO';
export const SET_FILTER = 'SET_FILTER';

export const addTodo = (id, text) => ({
  type: ADD_TODO,
  id,
  text,
});

export const toggleTodo = (id) => ({
  type: TOGGLE_TODO,
  id,
});

export const setFilter = (filter) => ({
  type: SET_FILTER,
  filter,
});
