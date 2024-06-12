function Es6_2() {
  // class
  class Animal {
    name: string;
    age: number;
    constructor(name: string, age: number) {
      this.name = name;
      this.age = age;
    }

    run() {
      console.log(`${this.name} is running`);
    }
  }

  class Dog extends Animal {
    breed: string;
    constructor(name: string, age: number, breed: string) {
      super(name, age);
      this.breed = breed;
    }

    bark() {
      super.run();
      return `${this.name} is barking`;
    }
  }

  const dog = new Dog("Buddy", 3, "Golden Retriever");

  // Map & WeakMap
  const map = new Map();
  const name = {},
    age = {},
    func = {};
  map.set(name, dog.name);
  map.set(age, dog.age);
  map.set(func, dog.bark);

  // Set & WeakSet
  const weakSet = new WeakSet();
  const obj = { name: "Bubble", age: 2 };
  weakSet.add(obj);
  weakSet.has(obj); // true
  weakSet.delete(obj); // 删除引用对象

  return (
    <div>
      <h1>{dog.bark()}</h1>
    </div>
  );
}

export default Es6_2;
