import { useFormikContext } from "formik";
import React from "react";
import { StyleSheet, View } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import RNPickerSelect from "react-native-picker-select";

import AppErrorMessage from "./AppErrorMessage";
import defaultStyles from "../../config/styles";
import AppText from "../AppText";
import colors from "../../config/colors";

function AppFormPickerField({ name, items, placeholder, suffix, icon, width }) {
  const { values, handleChange, errors, touched } = useFormikContext();

  return (
    <>
      <View style={styles.container}>
        {icon && (
          <MaterialCommunityIcons
            name={icon}
            size={20}
            color={defaultStyles.colors.medium}
            style={styles.icon}
          />
        )}
        <View style={styles.picker}>
          <RNPickerSelect
            placeholder={{ ...placeholder, color: colors.secondary }}
            items={items.map((item) => ({ ...item, color: colors.black }))}
            onValueChange={handleChange(name)}
            style={{ ...pickerSelectStyles }}
            value={values[name]}
          />
        </View>
        <AppText>
          {values[name] && values[name] !== placeholder.value && suffix}
        </AppText>
      </View>

      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
    justifyContent: "flex-start",
    backgroundColor: defaultStyles.colors.light,
    borderRadius: 25,
    flexDirection: "row",
    flex: 1,
    marginVertical: 10,
    paddingLeft: 10,
  },
  picker: {
    flex: 0.7,
  },
  textInput: {
    flex: 1,
  },
  icon: {
    marginRight: 10,
  },
});
const pickerSelectStyles = StyleSheet.create({
  inputIOS: {
    fontSize: 16,
    paddingVertical: 12,
    paddingHorizontal: 10,
    borderWidth: 1,
    borderColor: "gray",
    borderRadius: 4,
    color: "black",
    paddingRight: 30, // to ensure the text is never behind the icon
  },
  inputAndroid: {
    fontSize: 16,
    paddingHorizontal: 10,
    paddingVertical: 8,
    borderWidth: 0.5,
    borderColor: "purple",
    borderRadius: 8,
    color: "black",
    paddingRight: 30, // to ensure the text is never behind the icon
  },
});
export default AppFormPickerField;
