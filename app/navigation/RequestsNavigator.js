import React from "react";
import { View, StyleSheet } from "react-native";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import ScribeReceivedScreen from "../screens/ScribeReceivedScreen";
import ScribeAcceptedScreen from "../screens/ScribeAcceptedScreen";
import ScribeArchiveScreen from "../screens/ScribeArchiveScreen";

const Tab = createMaterialTopTabNavigator();

function RequestsNavigator(props) {
  return (
    <Tab.Navigator>
      <Tab.Screen name="Received" component={ScribeReceivedScreen} />
      <Tab.Screen name="Accepted" component={ScribeAcceptedScreen} />
      <Tab.Screen name="Archive" component={ScribeArchiveScreen} />
    </Tab.Navigator>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default RequestsNavigator;
