import React from "react";
import { StyleSheet } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { MaterialCommunityIcons } from "@expo/vector-icons";

import ScribeSearchScreen from "../screens/ScribeSearchScreen";
import AccountScreen from "../screens/AccountScreen";
import NotificationsScreen from "../screens/NotificationsScreen";
import RequestsNavigator from "./RequestsNavigator";
import AuthContext from "../auth/context";

const Tab = createBottomTabNavigator();

/**
 * Application navigator to be shown for valid user
 * @param {props} props
 * @returns
 */
function AppNavigator(props) {
  return (
    <AuthContext.Consumer>
      {({ isScribe }) => (
        <Tab.Navigator>
          <Tab.Screen
            name="Requests"
            component={RequestsNavigator}
            options={{
              tabBarIcon: ({ color, size }) => (
                <MaterialCommunityIcons name="home" color={color} size={size} />
              ),
            }}
          />
          {!isScribe && (
            <Tab.Screen
              name="Search"
              component={ScribeSearchScreen}
              options={{
                tabBarIcon: ({ color, size }) => (
                  <MaterialCommunityIcons
                    name="account-search-outline"
                    color={color}
                    size={size}
                  />
                ),
              }}
            />
          )}
          <Tab.Screen
            name="Notifications"
            component={NotificationsScreen}
            options={{
              tabBarIcon: ({ color, size }) => (
                <MaterialCommunityIcons
                  name="bell-outline"
                  color={color}
                  size={size}
                />
              ),
            }}
          />
          <Tab.Screen
            name="Account"
            component={AccountScreen}
            options={{
              tabBarIcon: ({ color, size }) => (
                <MaterialCommunityIcons
                  name="account"
                  color={color}
                  size={size}
                />
              ),
            }}
          />
        </Tab.Navigator>
      )}
    </AuthContext.Consumer>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AppNavigator;
