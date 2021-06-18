import React from "react";
import { View, StyleSheet, Pressable } from "react-native";
import DateTimePicker from "@react-native-community/datetimepicker";
import { useFormikContext } from "formik";
import { useState } from "react";
import moment from "moment";

import AppFormField from "./AppFormField";
import defaultStyles from "../../config/styles";
import AppErrorMessage from "./AppErrorMessage";

function AppFormDateTimePicker({ name, width, mode, ...otherProps }) {
  const { setFieldValue, values, errors, touched } = useFormikContext();
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [dateTime, setDateTime] = useState(new Date());
  const [pickerMode, setPickerMode] = useState(mode);

  const onDateSelection = (event, selectedDate) => {
    setShowDatePicker(Platform.OS === "ios");
    if (selectedDate) {
      setFieldValue(name, selectedDate);
      setDateTime(selectedDate);
      if (pickerMode === "date") {
        setPickerMode("time");
        setShowDatePicker(true);
      } else {
        setPickerMode("date");
      }
    }
  };

  return (
    <View style={styles.container}>
      {showDatePicker && (
        <DateTimePicker
          name="dateTimePicker"
          value={dateTime ? dateTime : new Date()}
          mode={pickerMode}
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
            values[name]
              ? moment(values[name]).format("MMMM Do YYYY, h:mm A")
              : ""
          }
        />
      </Pressable>
      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
});

export default AppFormDateTimePicker;
