import { useFormikContext } from "formik";
import React, { useState } from "react";
import { StyleSheet, View } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Picker } from "@react-native-picker/picker";

import AppErrorMessage from "./AppErrorMessage";
import defaultStyles from "../../config/styles";

function AppFormPickerField({
  name,
  items,
  placeholder,
  icon,
  width,
  ...otherProps
}) {
  const { setFieldTouched, handleChange, errors, touched } = useFormikContext();

  const [selectedValue, setSelectedValue] = useState(placeholder.value);
  const [selectedMinutes, setSelectedMinutes] = useState("minutes");

  const pickerItems = [];
  const minutes = [];

  for (let item of items) {
    pickerItems.push(
      <Picker.Item
        label={item.label.toString()}
        value={item.value.toString()}
      ></Picker.Item>
    );
  }

  for (let minute = 0; minute <= 59; minute++) {
    minutes.push(
      <Picker.Item
        label={minute.toString()}
        value={minute.toString()}
      ></Picker.Item>
    );
  }

  const handleHoursSelection = (itemValue, itemIndex) => {
    setSelectedValue(itemValue);
    handleChange(name);
  };

  const handleMinutesSelection = (itemValue, itemIndex) => {
    setSelectedMinutes(itemValue);
    handleChange(name);
  };

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
        <Picker
          mode="dropdown"
          onBlur={() => setFieldTouched(name)}
          selectedValue={selectedValue}
          style={styles.duration}
          onValueChange={handleHoursSelection}
        >
          <Picker.Item label={placeholder.label} value={placeholder.value} />
          {pickerItems}
        </Picker>
      </View>

      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: defaultStyles.colors.light,
    borderRadius: 25,
    flexDirection: "row",
    padding: 15,
    marginVertical: 10,
    justifyContent: "center",
  },
  icon: {
    marginRight: 10,
  },
  duration: {
    height: 25,
    width: 150,
  },
});

export default AppFormPickerField;
