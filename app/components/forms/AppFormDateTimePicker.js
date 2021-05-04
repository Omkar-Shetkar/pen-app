import React from "react";
import { View, StyleSheet, Pressable } from "react-native";
import DateTimePicker from "@react-native-community/datetimepicker";
import { useFormikContext } from "formik";
import { useState } from "react";
import moment from "moment";

import AppFormField from "./AppFormField";
import defaultStyles from "../../config/styles";

function AppFormDateTimePicker({ name, width, mode, ...otherProps }) {
  const {
    setFieldTouched,
    setFieldValue,
    handleChange,
    values,
    errors,
    touched,
  } = useFormikContext();
  const [showDatePicker, setShowDatePicker] = useState(false);

  const onDateSelection = (event, selectedDate) => {
    setShowDatePicker(Platform.OS === "ios");
    setFieldValue(name, selectedDate);
  };

  const format = (mode) => {
    switch (mode) {
      case "date":
        return "MMMM Do YYYY";
      case "time":
        return "LT";
    }
  };
  return (
    <View style={styles.container}>
      {showDatePicker && (
        <DateTimePicker
          name="dateTimePicker"
          value={new Date()}
          mode={mode}
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
          editable={false}
          color={defaultStyles.colors.medium}
          {...otherProps}
          value={
            values[name] ? moment(values[name]).format(format(mode)) : false
          }
        />
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AppFormDateTimePicker;
