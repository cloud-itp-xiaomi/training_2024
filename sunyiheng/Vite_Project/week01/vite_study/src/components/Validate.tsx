function Validate() {
  function asyncFunc(name) {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(name);
      }, 1000);
    });
  }

  function* generatorFunc(name) {
    const userName = yield asyncFunc(name);
    console.log(userName);
    // fetch 本身是异步的，其返回一个 promise 对象，这个 promise 对象在请求完成后会解析为一个 Response 对象
    const response = yield fetch(`https://api.github.com/users/${userName}`);
    // response.json() 方法也是异步的，也是返回一个 promise 对象
    const userData = yield response.json();
    return userData;
  }

  const gen = generatorFunc("github");
  gen
    .next()
    .value.then((data) => gen.next(data).value)
    .then((response) => gen.next(response).value)
    .then((userData) => console.log(userData));
}

export default Validate;
