import { useState } from "react";

function Es6_4() {
  let arr = [1, 2, 3, 4, 5];
  let itr = arr[Symbol.iterator]();
  const [userData1, setUserData1] = useState("");
  const [userData2, setUserData2] = useState("");

  function* getResult() {
    try {
      let result = yield fetch("https://api.github.com/users/github");
      setUserData1(result);
      result = yield fetch("https://api.github.com/users/alex");
      setUserData2(result);
    } catch (error) {
      console.log(error);
    }
  }

  function run(generator, data) {
    let next = generator.next(data);

    function handleResult(result) {
      if (result.done) {
        return;
      }
      result.value
        .then((res) => res.json())
        .then((data) => {
          next = generator.next(data);
          handleResult(next);
        })
        .catch((error) => {
          generator.throw(error);
        });
    }

    handleResult(next);
  }

  run(getResult(), null);

  return (
    <div>
      <ul>
        <li>
          {userData1.login}
          <img
            style={{ height: 100, width: 100, marginLeft: 50 }}
            src={userData1.avatar_url}
            alt="github"
          />
        </li>

        <li>
          {userData2.login}
          <img
            style={{ height: 100, width: 100, marginLeft: 50 }}
            src={userData2.avatar_url}
            alt="alex"
          />
        </li>
      </ul>
    </div>
  );
}

export default Es6_4;
