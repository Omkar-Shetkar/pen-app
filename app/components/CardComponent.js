import React from "react";
import { View, StyleSheet } from "react-native";
import { TouchableWithoutFeedback } from "react-native-gesture-handler";
import { MaterialCommunityIcons } from "@expo/vector-icons";

import AppText from "./AppText";
import colors from "../config/colors";
import defaultStyles from "../config/styles";

function CardComponent({ title, subTitle, thumbnailIcon }) {
  return (
    <TouchableWithoutFeedback>
      <View style={styles.card}>
        <View style={styles.icon}>
          <MaterialCommunityIcons
            name={thumbnailIcon}
            size={35}
            color={defaultStyles.colors.medium}
          />
        </View>
        <View style={styles.detailsContainer}>
          <AppText numberOfLines={1} style={styles.title}>
            {title}
          </AppText>
          <AppText numberOfLines={2} style={styles.subTitle}>
            {subTitle}
          </AppText>
        </View>
      </View>
    </TouchableWithoutFeedback>
  );
}
const styles = StyleSheet.create({
  card: {
    alignItems: "center",
    backgroundColor: colors.white,
    marginBottom: 20,
    borderRadius: 15,
    flexDirection: "row",
    justifyContent: "flex-start",
    marginBottom: 20,
    overflow: "hidden",
  },
  detailsContainer: {
    padding: 20,
  },
  icon: {
    alignItems: "center",
    backgroundColor: colors.whiteSmoke,
    borderRadius: 25,
    height: 50,
    justifyContent: "center",
    width: 50,
  },
  subTitle: {
    color: colors.secondary,
    fontWeight: "bold",
  },
  title: {
    marginBottom: 7,
  },
});
export default CardComponent;
