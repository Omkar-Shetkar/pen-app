import React, { useState } from "react";
import { useFormikContext } from "formik";
import { View, StyleSheet, Pressable } from "react-native";
import { RadioButton } from "react-native-paper";

import AppText from "../AppText";

function AppFormRadioButtons({ name, data, onSelection }) {
  const [checked, setChecked] = useState("");
  const { values } = useFormikContext();

  values[name] = checked;

  const handleOnPress = (option) => {
    return () => {
      setChecked(option.value);
      values[name] = option.value;
      onSelection(option);
    };
  };

  const options = data.map((option) => (
    <Pressable style={styles.optionContainer} onPress={handleOnPress(option)}>
      <RadioButton
        value={option.value}
        status={checked === option.value ? "checked" : "unchecked"}
        onPress={handleOnPress(option)}
      />
      <AppText>{option.label}</AppText>
    </Pressable>
  ));

  return <View style={styles.container}>{options}</View>;
}

const styles = StyleSheet.create({
  container: {
    flexDirection: "row",
    justifyContent: "space-evenly",
  },
  optionContainer: { flexDirection: "row", alignItems: "center" },
});

export default AppFormRadioButtons;
