import React from "react";
import { StyleSheet } from "react-native";
import AppDateTimePicker from "./app/components/AppDateTimePicker";
import Sample from "./app/components/Sample";
import ScribeSearchScreen from "./app/screens/ScribeSearchScreen";
import LoginScreen from "./app/screens/LoginScreen";

export default function App() {
  // return <AppDateTimePicker></AppDateTimePicker>;
  return <ScribeSearchScreen></ScribeSearchScreen>;
  // return <LoginScreen></LoginScreen>;
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
