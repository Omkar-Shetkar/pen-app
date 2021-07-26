import React from "react";

import { createStackNavigator } from "@react-navigation/stack";
import ScribeSearchScreen from "../screens/ScribeSearchScreen";
import ScribeSearchResultsScreen from "../screens/ScribeSearchResultsScreen";

const Stack = createStackNavigator();

const ScribeSearchNavigator = () => (
  <Stack.Navigator mode="card" screenOptions={{ headerShown: false }}>
    <Stack.Screen name="Search" component={ScribeSearchScreen} />
    <Stack.Screen
      name="ScribeSearchResults"
      component={ScribeSearchResultsScreen}
    />
  </Stack.Navigator>
);
export default ScribeSearchNavigator;
