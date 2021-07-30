import React, { useContext } from "react";
import { StyleSheet } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { MaterialCommunityIcons } from "@expo/vector-icons";

import AccountScreen from "../screens/AccountScreen";
import RequestsNavigator from "./RequestsNavigator";
import AuthContext from "../auth/context";
import ScribeSearchNavigator from "./ScribeSearchNavigator";
import routes from "./routes";
import SearchButton from "./SearchButton";

const Tab = createBottomTabNavigator();

/**
 * Application navigator to be shown for valid user
 * @param {props} props
 * @returns
 */
function AppNavigator(props) {
  const { user, setUser, isScribe } = useContext(AuthContext);

  console.log("AppNavigator --> isScribe", isScribe);
  return (
    <AuthContext.Consumer>
      {({ isScribe }) => (
        <Tab.Navigator
          initialRouteName={!isScribe ? "Search" : ""}
          tabBarOptions={{
            labelStyle: { fontWeight: "800" },
            tabStyle: {
              alignContent: "center",
            },
          }}
        >
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
              component={ScribeSearchNavigator}
              options={({ navigation }) => ({
                tabBarIcon: ({ color }) => (
                  <SearchButton
                    color={color}
                    onPress={() => {
                      navigation.navigate(routes.SCRIBE_SEARCH);
                    }}
                  />
                ),
              })}
            />
          )}
          {/* <Tab.Screen
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
          /> */}
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
