import React from "react";
import { StyleSheet } from "react-native";

import AppText from "../AppText";

function AppErrorMessage({ error, visible }) {
  if (!visible || !error) return null;
  return <AppText style={styles.error}>{error}</AppText>;
}

const styles = StyleSheet.create({
  error: { flex: 0.25, color: "red", fontSize: 14 },
});
export default AppErrorMessage;
