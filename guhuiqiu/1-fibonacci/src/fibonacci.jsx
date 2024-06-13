import React from "react";
import ReactDOM from "react-dom/client";
import bigInt from "big-integer";

function fibonacci(n) {
  if (n === 0) return bigInt(0);
  if (n === 1) return bigInt(1);

  let a = bigInt(0);
  let b = bigInt(1);

  for (let i = 2; i <= n; i++) {
    let temp = b;
    b = a.add(b);
    a = temp;
  }

  return b;
}

console.log("Fibonacci of 10000:", fibonacci(10000).toString());

function App() {
  return (
    <div>
      <h1>Check the console for the Fibonacci result</h1>
    </div>
  );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
