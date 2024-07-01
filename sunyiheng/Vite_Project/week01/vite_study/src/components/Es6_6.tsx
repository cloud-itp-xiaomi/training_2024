// 验证 ES6 的 ArrayBuffer 特性

function Es6_6() {
  async function getArrayBuffer() {
    try {
      const response = await fetch("https://api.github.com/users");
      return response.arrayBuffer();
    } catch (error) {
      console.error("There was a problem with the fetch operation:", error);
      throw error;
    }
  }

  async function parseArrayBuffer(buffer) {
    try {
      // 利用原生 TextDecoder 对数据进行解码
      const decoder = new TextDecoder("utf-8");
      const json = decoder.decode(buffer);
      const data = JSON.parse(json);
      return data;
    } catch (error) {
      console.error("Error parsing ArrayBuffer:", error);
      throw error;
    }
  }

  getArrayBuffer()
    .then(parseArrayBuffer)
    .then((data) => console.log(data))
    .catch((error) => console.log("Promise chain eeror", error));
}

export default Es6_6;
