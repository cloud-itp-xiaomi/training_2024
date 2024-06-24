// eslint-disable-next-line no-unused-vars
import React from 'react';
import { connect } from 'react-redux';
import { addTodo } from '../actions';
import { Input, Button, Form } from 'antd';

// 定义 AddTodo 容器组件，用于添加新待办事项
// eslint-disable-next-line react/prop-types
let AddTodo = ({ dispatch }) => {
  let input;

  const onFinish = (values) => {
    if (!values.todo.trim()) {
      return; // 如果输入为空则不添加待办事项
    }
    dispatch(addTodo(values.todo)); // 分发 addTodo 动作
    input.state.value = ""; // 清空输入框
  };

  return (
    <div>
      <Form
        onFinish={onFinish}
        layout="inline"
      >
        <Form.Item
          name="todo"
          rules={[{ required: true, message: 'Please input your todo!' }]}
        >
          <Input
            placeholder="Add a new todo"
            ref={(node) => {
              input = node; // 绑定输入框引用
            }}
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">
            Add Todo
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

// 将 AddTodo 组件连接到 Redux store
AddTodo = connect()(AddTodo);

// 导出 AddTodo 容器组件
export default AddTodo;

