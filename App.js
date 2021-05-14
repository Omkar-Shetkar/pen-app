import React from "react";
import { StyleSheet } from "react-native";
import { NavigationContainer } from "@react-navigation/native";

import ScribeReceivedScreen from "./app/screens/ScribeReceivedScreen";
import ScribeSearchScreen from "./app/screens/ScribeSearchScreen";
import AccountScreen from "./app/screens/AccountScreen";
import RegisterScreen from "./app/screens/RegisterScreen";
import AppNavigator from "./app/navigation/AppNavigator";

export default function App() {
  return (
    <NavigationContainer>
      <AppNavigator />
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    alignItems: "center",
    justifyContent: "center",
  },
});
