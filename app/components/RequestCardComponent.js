import React from "react";
import { View, StyleSheet } from "react-native";
import { TouchableWithoutFeedback } from "react-native-gesture-handler";
import { MaterialCommunityIcons } from "@expo/vector-icons";

import AppText from "./AppText";
import colors from "../config/colors";
import defaultStyles from "../config/styles";
import ListItemButton from "./ListItemButton";

function RequestCardComponent({
  actions,
  title,
  subTitle,
  details,
  icons,
  thumbnailIcon,
}) {
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
          <AppText numberOfLines={2} style={styles.title}>
            {title}
          </AppText>
          <AppText numberOfLines={2} style={styles.subTitle}>
            {subTitle}
          </AppText>
          {Object.keys(details).map((key) => (
            <View>
              <MaterialCommunityIcons
                name={icons[key]}
                size={15}
                color={defaultStyles.colors.medium}
              />
              <AppText numberOfLines={5} style={styles.title}>
                {details[key]}
              </AppText>
            </View>
          ))}
        </View>
        <View style={styles.button}>
          {actions &&
            actions.map((item) => (
              <ListItemButton
                title={item.name}
                onPress={item.action}
                color={item.color}
              />
            ))}
        </View>
      </View>
    </TouchableWithoutFeedback>
  );
}
const styles = StyleSheet.create({
  card: {
    backgroundColor: colors.white,
    marginBottom: 20,
    borderRadius: 15,
    elevation: 2,
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
    marginVertical: 20,
    width: 50,
  },
  subTitle: {
    color: colors.secondary,
    fontWeight: "bold",
  },
  title: {
    marginBottom: 7,
  },
  button: {
    alignItems: "center",
    justifyContent: "center",
  },
});

export default RequestCardComponent;
