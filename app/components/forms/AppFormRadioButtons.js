import React, { useState } from "react";
import { useFormikContext } from "formik";
import { View, StyleSheet, Pressable } from "react-native";
import { RadioButton } from "react-native-paper";

import AppText from "../AppText";
import AppErrorMessage from "./AppErrorMessage";
import colors from "../../config/colors";

function AppFormRadioButtons({ name, data, onSelection }) {
  const [checked, setChecked] = useState("");
  const { values, errors, touched } = useFormikContext();

  values[name] = checked;

  const handleOnPress = (option) => {
    return () => {
      setChecked(option.value);
      values[name] = option.value;
      onSelection(option);
    };
  };

  const options = data.map((option) => (
    <>
      <Pressable
        style={[
          styles.optionbuttonContainer,
          {
            backgroundColor:
              checked === option.value ? colors.secondary : colors.light,
          },
        ]}
        onPress={handleOnPress(option)}
      >
        <RadioButton
          value={option.value}
          status={checked === option.value ? "checked" : "unchecked"}
          onPress={handleOnPress(option)}
        />
        <AppText
          style={[
            styles.label,
            checked === option.value
              ? { fontWeight: "bold" }
              : { fontWeight: "800" },
          ]}
        >
          {option.label}
        </AppText>
      </Pressable>
      <View>
        {checked === option.value && (
          <AppText style={styles.description}>{option.description}</AppText>
        )}
      </View>
    </>
  ));

  return (
    <View style={styles.container}>
      {options}
      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flexDirection: "column",
    justifyContent: "center",
  },
  optionbuttonContainer: {
    flexDirection: "row",
    alignItems: "center",
    borderWidth: 0.5,
    borderRadius: 30,
    borderColor: colors.medium,
    margin: 5,
  },
  label: {
    color: colors.primary,
    fontWeight: "800",
  },
  description: {
    color: colors.medium,
    fontWeight: "600",
  },
});

export default AppFormRadioButtons;
