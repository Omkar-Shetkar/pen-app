import React from "react";
import { View, StyleSheet } from "react-native";
import { TouchableWithoutFeedback } from "react-native-gesture-handler";
import colors from "../config/colors";
import AppText from "./AppText";

function NotificationCardComponent({ message }) {
  return (
    <TouchableWithoutFeedback>
      <View style={styles.card}>
        <AppText numberOfLines={3} style={styles.content}>
          {message}
        </AppText>
      </View>
    </TouchableWithoutFeedback>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.whiteSmoke,
    marginBottom: 20,
    borderRadius: 15,
    elevation: 2,
    flexDirection: "row",
    justifyContent: "flex-start",
    marginBottom: 20,
    overflow: "hidden",
  },
  content: {
    padding: 20,
    flex: 0.8,
  },
});

export default NotificationCardComponent;
