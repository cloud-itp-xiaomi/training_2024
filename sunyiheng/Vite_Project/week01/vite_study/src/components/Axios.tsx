import React, { Component } from 'react'
import axios from 'axios';

export default class Axios extends Component {
  getStudentData = () => {
    axios.get().then(
      response => {console.log('成功了', response.data);
      error => {console.log('失败了', error);
      }
      }
    )
  }
  render() {
    return (
      <div>
        <button onClick={this.getStudentData}></button>
      </div>
    )
  }
}
