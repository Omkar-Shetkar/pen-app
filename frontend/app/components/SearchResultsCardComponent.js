import React from "react";
import { View, StyleSheet } from "react-native";
import { Fontisto } from "@expo/vector-icons";

import AppText from "./AppText";
import colors from "../config/colors";
import defaultStyles from "../config/styles";
import ListItemButton from "./ListItemButton";
import strings from "../config/strings";

function SearchResultsCardComponent({
  title,
  subTitle,
  onPress,
  thumbnailIcon,
}) {
  return (
    <>
      <View style={styles.card}>
        <View style={styles.icon}>
          <Fontisto
            name={thumbnailIcon}
            size={35}
            color={defaultStyles.colors.medium}
          />
        </View>
        <View
          style={styles.detailsContainer}
          accessible={true}
          accessibilityLabel={title + ", " + strings.search.a11y.result}
        >
          <AppText numberOfLines={2} style={styles.title}>
            {title}
          </AppText>
          <AppText numberOfLines={2} style={styles.subTitle}>
            {subTitle}
          </AppText>
        </View>
        <View style={styles.button}>
          <ListItemButton
            title="Request"
            accessible={true}
            accessibilityLabel={strings.request.a11y.sendRequest + ", " + title}
            onPress={() => onPress()}
          />
        </View>
      </View>
    </>
  );
}
const styles = StyleSheet.create({
  card: {
    alignItems: "center",
    backgroundColor: colors.white,
    marginBottom: 20,
    borderRadius: 15,
    elevation: 5,
    flexDirection: "row",
    justifyContent: "flex-start",
    marginBottom: 20,
    overflow: "hidden",
  },
  detailsContainer: {
    padding: 20,
    flex: 0.8,
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
    fontWeight: "500",
    fontStyle: "italic",
  },
  title: {
    fontWeight: "500",
    marginBottom: 7,
  },
  button: {
    alignItems: "center",
    justifyContent: "center",
  },
});
export default SearchResultsCardComponent;
