import React from "react";
import { View, StyleSheet } from "react-native";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import ScribeReceivedScreen from "../screens/ScribeReceivedScreen";
import ScribeAcceptedScreen from "../screens/ScribeAcceptedScreen";
import ScribeArchiveScreen from "../screens/ScribeArchiveScreen";
import CandidateAppliedScreen from "../screens/CandidateAppliedScreen";
import CandidateAcceptedScreen from "../screens/CandidateAcceptedScreen";
import CandidateArchiveScreen from "../screens/CandidateArchiveScreen";
import AuthContext from "../auth/context";

const Tab = createMaterialTopTabNavigator();

function RequestsNavigator(props) {
  return (
    <AuthContext.Consumer>
      {({ isScribe }) => (
        <Tab.Navigator>
          {isScribe && (
            <>
              <Tab.Screen name="Received" component={ScribeReceivedScreen} />
              <Tab.Screen name="Accepted" component={ScribeAcceptedScreen} />
              <Tab.Screen name="Archive" component={ScribeArchiveScreen} />
            </>
          )}

          {!isScribe && (
            <>
              <Tab.Screen name="Applied" component={CandidateAppliedScreen} />
              <Tab.Screen name="Accepted" component={CandidateAcceptedScreen} />
              <Tab.Screen name="Archive" component={CandidateArchiveScreen} />
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
