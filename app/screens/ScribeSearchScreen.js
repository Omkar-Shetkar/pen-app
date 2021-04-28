import React, { useState } from "react";
import { Pressable, StyleSheet } from "react-native";

import Screen from "../components/Screen";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import SubmitButton from "../components/forms/SubmitButton";
import DateTimePicker from "@react-native-community/datetimepicker";
import moment from "moment";
import AppFormDurationField from "../components/forms/AppFormDurationField";

function ScribeSearchScreen(props) {
  const [error, setError] = useState();
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showTimePicker, setShowTimePicker] = useState(false);
  const [date, setDate] = useState();
  const [time, setTime] = useState();
  const [mode, setMode] = useState("date");

  const onDateSelection = (event, selectedDate) => {
    const currentDate = selectedDate || date;
    setShowDatePicker(Platform.OS === "ios");
    setDate(currentDate);
  };

  const onTimeSelection = (event, selectedDate) => {
    const currentDate = selectedDate || date;
    setShowTimePicker(Platform.OS === "ios");
    setTime(currentDate);
  };

  return (
    <Screen style={styles.container}>
      <AppForm>
        <AppErrorMessage error={error} visible={error} />
        {showDatePicker && (
          <DateTimePicker
            value={new Date()}
            mode={"date"}
            is24Hour={false}
            display="default"
            onChange={onDateSelection}
          />
        )}
        {showTimePicker && (
          <DateTimePicker
            value={new Date()}
            mode={"time"}
            is24Hour={false}
            display="default"
            onChange={onTimeSelection}
          />
        )}
        <Pressable onPress={() => setShowDatePicker(true)}>
          <AppFormField
            icon="calendar"
            name="examDate"
            placeholder="Exam Date"
            editable={false}
            value={date ? date.toDateString() : false}
          />
        </Pressable>

        <Pressable onPress={() => setShowTimePicker(true)}>
          <AppFormField
            icon="clock-outline"
            name="examTime"
            editable={false}
            placeholder="Exam Time"
            value={time ? moment(time).format("LT") : false}
          />
        </Pressable>

        <AppFormDurationField
          icon="av-timer"
          name="duration"
          placeholder="Duration"
        />

        <AppFormField
          autoCorrect={false}
          icon="alpha"
          name="language"
          placeholder="Language"
        />
        <AppFormField
          autoCorrect={false}
          icon="map-marker-outline"
          name="location"
          placeholder="Exam Location"
        />
        <SubmitButton title="Search" />
      </AppForm>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default ScribeSearchScreen;
