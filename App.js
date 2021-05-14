import React, { useEffect, useState } from "react";
import { StyleSheet } from "react-native";
import { NavigationContainer } from "@react-navigation/native";

import ScribeReceivedScreen from "./app/screens/ScribeReceivedScreen";
import ScribeSearchScreen from "./app/screens/ScribeSearchScreen";
import AccountScreen from "./app/screens/AccountScreen";
import RegisterScreen from "./app/screens/RegisterScreen";
import AppNavigator from "./app/navigation/AppNavigator";
import AuthContext from "./app/auth/context";

export default function App() {
  const [isScribe, setIsScribe] = useState(false);

  return (
    <AuthContext.Provider value={{ isScribe, setIsScribe }}>
      <NavigationContainer>
        <AppNavigator />
      </NavigationContainer>
    </AuthContext.Provider>
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
