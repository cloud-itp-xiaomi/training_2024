import { useEffect, useState } from "react";
import reactLogo from "../assets/react.svg";
function Es6_3() {
  const handler = {
    get(target, key) {
      invariant(key, "get");
      return target[key];
    },

    set(target, key, value) {
      invariant(key, "set");
      target[key] = value;
      return true;
    },

    apply(target, ctx, args) {
      return Reflect.apply(...arguments).map((item) => item * 2);
    },
  };

  function invariant(key, action) {
    if (key[0] === "_") {
      throw new Error(`Invalid attempt to ${action} private "${key}" property`);
    }
  }

  const target1 = {};
  const proxy1 = new Proxy(target1, handler);
  // proxy._name = "孙熠衡"; // 无效设置
  proxy1.age = 24;

  const target2 = (...args) => {
    return args.filter((item) => item % 2 === 0);
  };
  const proxy2 = new Proxy(target2, handler);

  const [imageUrl, setImageUrl] = useState("");

  const preLoadImage = function (path) {
    return new Promise((resolve, reject) => {
      const image = new Image();
      image.onload = () => {
        return resolve(image);
      };
      image.onerror = () => {
        return reject(new Error("预加载图片失败"));
      };
      image.src = path;
    });
  };

  useEffect(() => {
    preLoadImage(reactLogo)
      .then((image) => {
        setImageUrl(image.src);
      })
      .catch((error) => {
        console.log(error.message);
      });
  }, []);

  return (
    <div>
      {/* <h1>{proxy.name}</h1> */}
      <h2>{proxy1.age}</h2>
      <h3>{proxy2(1, 2, 3, 4, 5, 6)}</h3>
      {/* <img src="../assets/react.svg" alt="" /> */}
      {imageUrl && <img src={imageUrl} alt="加载的图片" />}
      {!imageUrl && <p>加载中...</p>}
    </div>
  );
}

export default Es6_3;
