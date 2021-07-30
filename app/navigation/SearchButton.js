import React from "react";
import { View, StyleSheet } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import colors from "../config/colors";
import { TouchableOpacity } from "react-native-gesture-handler";

function SearchButton({ color, onPress }) {
  return (
    <TouchableOpacity onPress={onPress}>
      <View style={styles.container}>
        <MaterialCommunityIcons name="account-search" color={color} size={28} />
      </View>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    backgroundColor: colors.secondary,
    borderColor: colors.white,
    borderWidth: 10,
    borderRadius: 33,
    bottom: 10,
    height: 65,
    justifyContent: "center",
    width: 65,
  },
});

export default SearchButton;
