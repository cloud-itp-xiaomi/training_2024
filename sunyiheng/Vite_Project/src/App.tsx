import React from "react";
import Header from "./containers/Header";
import Content from "./containers/Content";
import Footer from "./containers/Footer";

export default function App() {
  return (
    <div>
      <Header />
      <hr />
      <Content />
      <hr />
      <Footer />
    </div>
  );
}
