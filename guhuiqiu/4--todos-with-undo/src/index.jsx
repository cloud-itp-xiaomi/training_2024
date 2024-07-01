// eslint-disable-next-line no-unused-vars
import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import rootReducer from './reducers';
import App from './components/App.jsx';



// 使用 configureStore 创建 store
const store = configureStore({
  reducer: rootReducer,
});

// 获取要挂载的根节点
const rootElement = document.getElementById("root");

// 使用 createRoot 创建根
const root = createRoot(rootElement);

// 渲染应用
root.render(
  <Provider store={store}>
    <App />
  </Provider>
);
