let myName = "Tom";
let myAge = 20;
let myfn = function () {
  return "My name is" + myName + "! I'm '" + myAge + " years old.";
};
let myClass = class myClass {
  static a = "yeah!";
};
// export 命令可以出现在模块的任何位置，但必需处于模块顶层
// export命令除了输出变量，还可以输出函数或类（class）
export { myName, myAge, myfn, myClass };
