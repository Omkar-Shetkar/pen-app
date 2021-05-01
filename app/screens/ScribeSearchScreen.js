import React, { useState } from "react";
import { Pressable, StyleSheet, View } from "react-native";

import Screen from "../components/Screen";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import SubmitButton from "../components/forms/SubmitButton";
import DateTimePicker from "@react-native-community/datetimepicker";
import moment from "moment";
import AppFormPickerField from "../components/forms/AppFormPickerField";

function ScribeSearchScreen(props) {
  const [error, setError] = useState();
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [showTimePicker, setShowTimePicker] = useState(false);
  const [date, setDate] = useState();
  const [time, setTime] = useState();

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

  const hours = [];
  for (let i = 0; i <= 12; i++) {
    hours.push({ label: i.toString(), value: i.toString() });
  }

  const minutes = [];
  for (let i = 0; i <= 59; i++) {
    minutes.push({ label: i.toString(), value: i.toString() });
  }

  const languages = [
    { label: "English", value: "english" },
    { label: "Hindi", value: "hindi" },
    { label: "Kannada", value: "kannada" },
  ];

  const exams = [
    { label: "SSLC", value: "sslc" },
    { label: "PUC", value: "puc" },
    { label: "BSc", value: "bsc" },
  ];
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

        <View style={styles.durations}>
          <AppFormPickerField
            icon="av-timer"
            name="durationHours"
            items={hours}
            placeholder={{ label: "Hours", value: "hours" }}
            suffix="hr"
          />

          <AppFormPickerField
            icon="av-timer"
            name="durationMinutes"
            items={minutes}
            placeholder={{ label: "Minutes", value: "minutes" }}
            suffix="min"
          />
        </View>

        <AppFormPickerField
          icon="alpha"
          name="language"
          items={languages}
          placeholder={{ label: "Language", value: "language" }}
        />

        <AppFormPickerField
          icon="tournament"
          name="language"
          items={exams}
          placeholder={{ label: "Exam", value: "exam" }}
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
  durations: {
    flexDirection: "row",
    width: "100%",
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: 15,
  },
});

export default ScribeSearchScreen;
