function Es6_1() {
  // 解构赋值
  const [a, b, c] = [1, 2, 3];

  const {
    userName,
    age: userAge,
    ...otherInfo
  } = {
    userName: "孙熠衡",
    age: 24,
    gender: "男",
    hobby: "骑行",
  };

  // 数组和对象扩展
  const arr1 = [1, 2, 3];
  const arr2 = [...arr1, 4, 5];
  const objA = {
    name: "孙熠衡",
  };
  const objB = {
    age: 24,
  };
  const objC = Object.assign({}, objA, objB);

  // 箭头函数
  const fn = (arr: Array<number>) => {
    const new_arr = arr.map((item) => {
      return item * 2;
    });
    return new_arr;
  };
  const new_arr = fn(arr2);

  return (
    <>
      <ul>
        <li>{a}</li>
        <li>{b}</li>
        <li>{c}</li>
        <li>{userName}</li>
        <li>{userAge}</li>
        <li>{otherInfo.gender}</li>
        <li>{otherInfo.hobby}</li>
      </ul>
      <div>{new_arr}</div>
      <div>{objC.name}</div>
    </>
  );
}

export default Es6_1;
