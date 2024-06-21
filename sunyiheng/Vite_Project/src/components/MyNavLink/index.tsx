import React from "react";
import { NavLink } from "react-router-dom";

const MyNavLink = ({ to, children }) => {
  return (
    <NavLink
      to={to}
      style={({ isActive }) => ({
        textDecoration: "none",
        color: "inherit",
        fontWeight: isActive ? "bold" : "normal",
      })}
    >
      {children}
    </NavLink>
  );
};

export default MyNavLink;
