import React, { useState } from "react";
import { ScrollView, StyleSheet, View } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import SubmitButton from "../components/forms/SubmitButton";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import AppFormDateTimePicker from "../components/forms/AppFormDateTimePicker";

const validationSchema = Yup.object().shape({
  examDate: Yup.date().required().label("examDate"),
  examDate: Yup.date().required().label("examDate"),
  durationHours: Yup.number().required().min(0).label("durationHours"),
  durationMinutes: Yup.number().required().min(0).label("durationMinutes"),
  language: Yup.string().required().label("language"),
  exam: Yup.string().required().label("exam"),
});

function ScribeSearchScreen(props) {
  const [error, setError] = useState();

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
    { label: "Others", value: "others" },
  ];

  return (
    <Screen style={styles.container}>
      <ScrollView>
        <AppForm
          initialValues={{
            examDate: "",
            examTime: "",
            durationHours: "",
            durationMinutes: "",
            language: "",
            exam: "",
          }}
          onSubmit={(values) => console.log(values)}
          validationSchema={validationSchema}
        >
          <AppErrorMessage error={error} visible={error} />

          <AppFormDateTimePicker
            icon="calendar"
            name="examDate"
            mode="date"
            placeholder="Exam Date"
            editable={false}
          />

          <AppFormDateTimePicker
            icon="calendar"
            name="examTime"
            mode="time"
            placeholder="Exam Time"
            editable={false}
          />

          <View style={styles.durations}>
            <AppFormPickerField
              icon="av-timer"
              name="durationHours"
              items={hours}
              placeholder={{
                label: "Hours",
                value: "hours",
              }}
              suffix="hr"
            />

            <AppFormPickerField
              icon="av-timer"
              name="durationMinutes"
              items={minutes}
              placeholder={{
                label: "Minutes",
                value: "minutes",
              }}
              suffix="min"
            />
          </View>

          <AppFormPickerField
            icon="alpha"
            name="language"
            items={languages}
            placeholder={{
              label: "Language",
              value: "language",
            }}
          />

          <AppFormPickerField
            icon="tournament"
            name="exam"
            items={exams}
            placeholder={{
              label: "Exam",
              value: "exam",
            }}
          />

          <AppFormField
            autoCorrect={false}
            icon="map-marker-outline"
            name="location"
            placeholder="Exam Location"
          />
          <SubmitButton title="Search" />
        </AppForm>
      </ScrollView>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
  durations: {
    flexDirection: "row",
    flex: 0.6,
    justifyContent: "space-evenly",
  },
});

export default ScribeSearchScreen;
