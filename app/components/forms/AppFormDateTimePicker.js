import React from "react";
import { View, StyleSheet, Pressable } from "react-native";
import DateTimePicker from "@react-native-community/datetimepicker";
import { useFormikContext } from "formik";
import { useState } from "react";

import AppFormField from "./AppFormField";
import defaultStyles from "../../config/styles";

function AppFormDateTimePicker({ name, width, ...otherProps }) {
  const {
    setFieldTouched,
    setFieldValue,
    handleChange,
    values,
    errors,
    touched,
  } = useFormikContext();
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [date, setDate] = useState();

  const onDateSelection = (event, selectedDate) => {
    console.log("selectedDate", selectedDate);
    const currentDate = selectedDate || date;
    setShowDatePicker(Platform.OS === "ios");
    setDate(currentDate);
    setFieldValue(name, currentDate);
    console.log("values[name]**********", values[name]);
  };

  return (
    <View style={styles.container}>
      {showDatePicker && (
        <DateTimePicker
          name="dateTimePicker"
          value={new Date()}
          mode={"date"}
          is24Hour={false}
          display="default"
          onChange={onDateSelection}
        />
      )}
      <Pressable onPress={() => setShowDatePicker(true)}>
        <AppFormField
          icon="calendar"
          name="examDate"
          name="examDateTime"
          placeholder="Exam Date"
          editable={false}
          color={defaultStyles.colors.medium}
          value={values[name] ? values[name].toDateString() : false}
        />
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AppFormDateTimePicker;
