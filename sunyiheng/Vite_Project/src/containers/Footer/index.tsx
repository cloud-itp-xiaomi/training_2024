import React from "react";

export default function Footer() {
  return (
    <div>
      <label>
        <input type="checkbox" />
      </label>
      <span>
        <span>已完成0</span> / 全部2
      </span>
      &nbsp;
      <button>清除已完成任务</button>
      <div>
        <button>撤销</button>&nbsp;
        <button>重做</button>
      </div>
    </div>
  );
}
