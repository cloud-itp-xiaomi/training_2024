import React from "react";
import { BrowserRouter } from "react-router-dom";
import Header from "./containers/Header";
import Content from "./containers/Content";
import Footer from "./containers/Footer";

export default function App() {
  return (
    <div>
      <BrowserRouter>
        <Header />
        <Content />
        <Footer />
      </BrowserRouter>
    </div>
  );
}
