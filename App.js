import React from "react";
import { StyleSheet } from "react-native";
import ScribeSearchScreen from "./app/screens/ScribeSearchScreen";

export default function App() {
  return <ScribeSearchScreen></ScribeSearchScreen>;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
