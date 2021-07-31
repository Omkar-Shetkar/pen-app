import React from "react";
import { StyleSheet } from "react-native";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import ScribeReceivedScreen from "../screens/ScribeReceivedScreen";
import ScribeAcceptedScreen from "../screens/ScribeAcceptedScreen";
import ScribeArchiveScreen from "../screens/ScribeArchiveScreen";
import CandidateAppliedScreen from "../screens/CandidateAppliedScreen";
import CandidateAcceptedScreen from "../screens/CandidateAcceptedScreen";
import CandidateArchiveScreen from "../screens/CandidateArchiveScreen";
import AuthContext from "../auth/context";
import colors from "../config/colors";
import strings from "../config/strings";

const Tab = createMaterialTopTabNavigator();

function RequestsNavigator(props) {
  return (
    <AuthContext.Consumer>
      {({ isScribe }) => (
        <Tab.Navigator
          tabBrOptions={{
            indicatorStyle: { height: 3 },
            labelStyle: { fontWeight: "600" },
            tabStyle: {
              width: 100,
              alignContent: "center",
            },
            style: {
              backgroundColor: colors.whiteSmoke,
              height: 50,
            },
          }}
        >
          {isScribe && (
            <>
              <Tab.Screen name="Received" component={ScribeReceivedScreen} />
              <Tab.Screen name="Accepted" component={ScribeAcceptedScreen} />
              <Tab.Screen name="Archive" component={ScribeArchiveScreen} />
            </>
          )}

          {!isScribe && (
            <>
              <Tab.Screen
                name="Applied"
                component={CandidateAppliedScreen}
                options={{
                  tabBarAccessibilityLabel: strings.request.a11y.appliedButton,
                }}
              />
              <Tab.Screen
                name="Accepted"
                component={CandidateAcceptedScreen}
                options={{
                  tabBarAccessibilityLabel: strings.request.a11y.acceptedButton,
                }}
              />
              <Tab.Screen
                name="Archive"
                component={CandidateArchiveScreen}
                options={{
                  tabBarAccessibilityLabel: strings.request.a11y.archiveButton,
                }}
              />
            </>
          )}
        </Tab.Navigator>
      )}
    </AuthContext.Consumer>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default RequestsNavigator;
