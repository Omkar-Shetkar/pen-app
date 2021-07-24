import React from "react";
import { View, StyleSheet, Pressable } from "react-native";
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
    <Pressable>
      <View style={styles.card}>
        <View style={styles.icon}>
          <MaterialCommunityIcons
            name={thumbnailIcon}
            size={35}
            color={defaultStyles.colors.medium}
          />
        </View>
        <View style={styles.content}>
          <AppText numberOfLines={2} style={styles.title}>
            {title}
          </AppText>

          <View style={styles.subTitleContainer}>
            {icons["subTitle"] && (
              <MaterialCommunityIcons
                name={icons["subTitle"]}
                size={15}
                color={defaultStyles.colors.medium}
              />
            )}
            <AppText numberOfLines={2} style={styles.subTitle}>
              {subTitle}
            </AppText>
          </View>

          {Object.keys(details).map((key) => (
            <View style={styles.details}>
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
    </Pressable>
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
  content: {
    padding: 20,
    flex: 0.8,
  },
  details: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 5,
    marginTop: 5,
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
  subTitleContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 10,
  },
  button: {
    alignItems: "center",
    justifyContent: "center",
  },
});

export default RequestCardComponent;
