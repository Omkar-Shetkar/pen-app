import { useFormikContext } from "formik";
import React, { useState } from "react";
import { StyleSheet, View } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Picker } from "@react-native-picker/picker";

import AppErrorMessage from "./AppErrorMessage";
import defaultStyles from "../../config/styles";

function AppFormDurationField({ name, icon, width, ...otherProps }) {
  const { setFieldTouched, handleChange, errors, touched } = useFormikContext();

  const [selectedHours, setSelectedHours] = useState("hours");
  const [selectedMinutes, setSelectedMinutes] = useState("minutes");

  const hours = [];
  const minutes = [];

  for (let hour = 0; hour <= 12; hour++) {
    hours.push(
      <Picker.Item
        label={hour.toString()}
        value={hour.toString()}
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
    setSelectedHours(itemValue);
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
          selectedValue={selectedHours}
          style={styles.duration}
          onValueChange={handleHoursSelection}
        >
          <Picker.Item label="Hours" value="hours" />
          {hours}
        </Picker>
        <Picker
          mode="dropdown"
          selectedValue={selectedMinutes}
          style={styles.duration}
          onValueChange={handleMinutesSelection}
        >
          <Picker.Item label="Minutes" value="minutes" />
          {minutes}
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
    alignItems: "center",
  },
  icon: {
    marginRight: 10,
  },
  duration: {
    height: 25,
    width: 150,
  },
});

export default AppFormDurationField;
