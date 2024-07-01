/*
 * @Author: futheworld 272761370@qq.com
 * @Date: 2024-06-23 21:49:10
 * @LastEditors: futheworld 272761370@qq.com
 * @LastEditTime: 2024-06-23 21:49:14
 * @FilePath: \todo-app\src\reducers\filters.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { SET_FILTER } from '../actions';

const filter = (state = 'all', action) => {
  switch (action.type) {
    case SET_FILTER:
      return action.filter;
    default:
      return state;
  }
};

export default filter;
