import { useState } from "react";

// 将 ES6_4 中的 Generator 异步函数改写成 async/await 函数
function Es6_5() {
  const [userName, setUserName] = useState("");
  const [userData, setUserData] = useState("");

  async function getUserInfo(name: string) {
    try {
      const userInfo = await fetch(`https://api.github.com/users/${name}`);
      return userInfo.json();
    } catch (error) {
      console.log(error);
    }
  }

  const handleInputChange = (e) => {
    setUserName(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (userName) {
      getUserInfo(userName).then((res) =>
        res.message ? setUserData(null) : setUserData(res)
      );
    } else {
      setUserData("请输入有效的用户名");
    }
  };

  console.log(userData);

  return (
    <div>
      <h1>GitHub 用户检索</h1>
      <form onSubmit={handleSubmit}>
        <label>
          输入要检索的用户名:
          <input type="text" value={userName} onChange={handleInputChange} />
        </label>
        <button type="submit">检索</button>
      </form>
      {userData !== null && (
        <div>
          <h2>用户名: {userData.login}</h2>
          <img
            src={userData.avatar_url}
            style={{ height: 100 }}
            alt="用户头像"
          />
          <h2>用户创建时间: {userData.created_at}</h2>
          <h2>粉丝: {userData.followers}</h2>
          <h2>正在关注: {userData.following}</h2>
          <h2>用户地址: {userData.location}</h2>
        </div>
      )}
      {userData === null && <h2>没有检索到该用户的信息</h2>}
    </div>
  );
}

export default Es6_5;
