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

  const pickerItems = [];

  for (let item of items) {
    pickerItems.push(
      <Picker.Item
        label={item.label.toString()}
        value={item.value.toString()}
      ></Picker.Item>
    );
  }

  const handleValueSelection = (itemValue, itemIndex) => {
    setSelectedValue(itemValue);
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
          onValueChange={handleValueSelection}
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
    padding: 15,
    flexDirection: "row",
    marginVertical: 10,
  },
  icon: {
    marginRight: 10,
  },
  duration: {
    height: 25,
    width: 120,
  },
});

export default AppFormPickerField;
