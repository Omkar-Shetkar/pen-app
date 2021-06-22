import React, { useState } from "react";
import { StyleSheet } from "react-native";
import { NavigationContainer } from "@react-navigation/native";

import AppNavigator from "./app/navigation/AppNavigator";
import AuthContext from "./app/auth/context";
import Toast from "react-native-toast-message";

export default function App() {
  const [isScribe, setIsScribe] = useState(false);

  return (
    <>
      <AuthContext.Provider value={{ isScribe, setIsScribe }}>
        <NavigationContainer>
          <AppNavigator />
        </NavigationContainer>
      </AuthContext.Provider>
      {/* Global toast message component initialization */}
      <Toast ref={(ref) => Toast.setRef(ref)} />
    </>
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
