// main.js

// Helper function to display results on the page
function displayResult(message) {
  const appDiv = document.getElementById("app");
  const p = document.createElement("p");
  p.textContent = message;
  appDiv.appendChild(p);
}

// 1. Let and Const
let variableLet = "This is a let variable";
const variableConst = "This is a const variable";
displayResult(variableLet);
displayResult(variableConst);

// 2. Arrow Functions
const arrowFunction = () => {
  displayResult("This is an arrow function");
};
arrowFunction();

// 3. Template Literals
const name = "ES6";
const templateLiteral = `Hello, ${name}!`;
displayResult(templateLiteral);

// 4. Default Parameters
function greet(message = "Hello, World!") {
  displayResult(message);
}
greet();
greet("Hi, there!");

// 5. Destructuring Assignment
const person = {
  firstName: "John",
  lastName: "Doe",
  age: 25,
};
const { firstName, lastName, age } = person;
displayResult(`Name: ${firstName} ${lastName}, Age: ${age}`);

// 6. Spread Operator
const arr1 = [1, 2, 3];
const arr2 = [...arr1, 4, 5, 6];
displayResult(`Array after spread: ${arr2}`);

// 7. Classes
class Person {
  constructor(name, age) {
    this.name = name;
    this.age = age;
  }
  greet() {
    displayResult(
      `Hello, my name is ${this.name} and I am ${this.age} years old.`
    );
  }
}
const personInstance = new Person("Alice", 30);
personInstance.greet();

// 8. Promises
const promise = new Promise((resolve, reject) => {
  setTimeout(() => {
    resolve("Promise resolved!");
  }, 1000);
});
promise.then((message) => {
  displayResult(message);
});

// 9. Modules (ES6 Module Syntax)
import { message } from "./module.js";
displayResult(message);

// 10. Enhanced Object Literals
const enhancedObject = {
  variableLet,
  variableConst,
  arrowFunction,
  greet(message = "Hello, Enhanced World!") {
    displayResult(message);
  },
};
displayResult(`Enhanced Object: ${JSON.stringify(enhancedObject)}`);
enhancedObject.greet();
