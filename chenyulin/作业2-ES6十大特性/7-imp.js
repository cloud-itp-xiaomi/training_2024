// import 命令会提升到整个模块的头部，首先执行
import { myName, myAge, myfn, myClass } from "./7-exp1.js";
// 通过 export 方式导出，在导入时要加{ }，export default 则不需要
import b from "./7-exp2.js";

console.log(myfn()); // My name is Tom! I'm 20 years old.
console.log(myAge); // 20
console.log(myName); // Tom
console.log(myClass.a); // yeah!

console.log(b);
