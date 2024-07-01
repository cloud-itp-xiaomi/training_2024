import { useState } from "react";

function Fibonacci() {
  // 定义状态来保存用户输入和计算结果
  const [number, setNumber] = useState("");
  const [result, setResult] = useState("");

  // 计算斐波那契数列第 n 项的函数
  const Fibonacci = (n: number): bigint => {
    if (n <= 0) return BigInt(0);
    if (n === 1) return BigInt(1);
    let a = BigInt(0),
      b = BigInt(1),
      fib = BigInt(1);
    for (let i = 2; i <= n; i++) {
      fib = a + b;
      a = b;
      b = fib;
    }
    return fib;
  };

  const formatScientific = (num: bigint): string => {
    const numStr = num.toString();
    const exponent = numStr.length - 1;
    const base = numStr[0] + "." + numStr.slice(1, 5); // 保留4位小数
    return `${base}e${exponent}`;
  };

  // 处理用户输入的变化
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNumber(e.target.value);
  };

  // 处理表单提交
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const n = parseInt(number, 10);
    if (!isNaN(n)) {
      const fibResult =
        String(Fibonacci(n)).length < 10
          ? String(Fibonacci(n))
          : formatScientific(Fibonacci(n));
      console.log(`斐波那契数列第 ${n} 项是: ${fibResult}`);
      setResult(fibResult);
    } else {
      setResult("请输入有效的数字");
    }
  };

  return (
    <div>
      <h1>斐波那契数列计算器</h1>
      <form onSubmit={handleSubmit}>
        <label>
          输入数字 n:
          <input type="text" value={number} onChange={handleInputChange} />
        </label>
        <button type="submit">计算</button>
      </form>
      {result !== null && (
        <div>
          <h2>结果: {result}</h2>
        </div>
      )}
    </div>
  );
}

export default Fibonacci;
