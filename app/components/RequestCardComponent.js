import React from "react";
import { View, StyleSheet, Pressable } from "react-native";
import { MaterialCommunityIcons, Fontisto } from "@expo/vector-icons";

import AppText from "./AppText";
import colors from "../config/colors";
import defaultStyles from "../config/styles";
import ListItemButton from "./ListItemButton";
import strings from "../config/strings";

function RequestCardComponent({
  actions,
  title,
  subTitle,
  details,
  thumbnailIcon,
}) {
  return (
    <Pressable>
      <View style={styles.card}>
        <View style={{ flexDirection: "row" }}>
          <View style={styles.icon}>
            <Fontisto
              name={thumbnailIcon}
              size={35}
              color={defaultStyles.colors.medium}
            />
          </View>
          <View style={styles.content}>
            <AppText
              numberOfLines={2}
              style={styles.title}
              accessible={true}
              accessibilityLabel={strings.request.a11y.message + title}
            >
              {title}
            </AppText>

            <View style={styles.examContainer}>
              <AppText
                numberOfLines={3}
                style={styles.exam}
                accessible={true}
                accessibilityLabel={
                  strings.request.a11y.exam +
                  subTitle +
                  strings.request.a11y.language +
                  details["language"]
                }
              >
                {subTitle} / {details["language"]}
              </AppText>
            </View>

            <View style={styles.details}>
              <View>
                <AppText
                  numberOfLines={5}
                  style={styles.time}
                  accessible={true}
                  accessibilityLabel={
                    strings.request.a11y.start +
                    ", " +
                    details["start"] +
                    ", " +
                    strings.request.a11y.end +
                    ", " +
                    details["end"]
                  }
                >
                  {details["start"]} {"\n"}-{"\n"} {details["end"]}
                </AppText>
              </View>
            </View>
          </View>
          <View style={styles.button}>
            {actions &&
              actions.map((item) => (
                <ListItemButton
                  title={item.name}
                  onPress={item.action}
                  color={item.color}
                  accessible={true}
                  accessibilityLabel={"Select to " + item.name + " the request"}
                />
              ))}
          </View>
        </View>
        <View style={styles.contactContainer}>
          <MaterialCommunityIcons
            name="map-marker-outline"
            size={20}
            color={defaultStyles.colors.medium}
          />
          <View style={styles.details}>
            <AppText
              numberOfLines={5}
              style={styles.address}
              accessible={true}
              accessibilityLabel={"address" + ", " + details["address"]}
            >
              {details["address"]}
            </AppText>
          </View>
          {details["mobile"] && (
            <View style={styles.details}>
              <MaterialCommunityIcons
                name="cellphone"
                size={15}
                color={defaultStyles.colors.medium}
              />
              <AppText
                numberOfLines={2}
                style={styles.address}
                accessible={true}
                accessibilityLabel={"mobile" + ", " + details["mobile"]}
              >
                {details["mobile"]}
              </AppText>
            </View>
          )}
        </View>
      </View>
    </Pressable>
  );
}
const styles = StyleSheet.create({
  address: {
    fontStyle: "italic",
    paddingLeft: 5,
  },
  card: {
    backgroundColor: colors.white,
    marginBottom: 20,
    borderRadius: 15,
    elevation: 5,
    flexDirection: "column",
    justifyContent: "flex-start",
    marginBottom: 20,
    overflow: "hidden",
  },
  contactContainer: {
    paddingLeft: 20,
  },
  content: {
    padding: 10,
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
  exam: {
    color: colors.primary,
    fontWeight: "800",
  },
  examContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 10,
  },
  button: {
    alignItems: "center",
    justifyContent: "center",
  },
  time: {
    color: colors.dark,
    fontWeight: "500",
  },
  title: {
    fontWeight: "400",
  },
});

export default RequestCardComponent;
