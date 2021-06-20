import React, { useEffect, useState } from "react";
import { ScrollView, StyleSheet, View } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import SubmitButton from "../components/forms/SubmitButton";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import AppFormDateTimePicker from "../components/forms/AppFormDateTimePicker";
import languagesApi from "../api/languages";
import examsApi from "../api/exams";
import scribesApi from "../api/scribes";
import useApi from "../hooks/useApi";
import strings from "../config/strings";
import routes from "../navigation/routes";

const validationSchema = Yup.object().shape({
  examDate: Yup.date()
    .min(new Date().toDateString())
    .required()
    .label("Exam date"),
  durationHours: Yup.number()
    .required("**")
    .min(0)
    .typeError("**")
    .label("Hours"),
  durationMinutes: Yup.number()
    .required("**")
    .min(0)
    .typeError("**")
    .label("Minutes"),
  language: Yup.string()
    .matches(/\b(?!language\b)\w+/, {
      message: strings.search.languageError,
    })
    .required()
    .label("Language"),
  exam: Yup.string()
    .matches(/\b(?!exam\b)\w+/, {
      message: strings.search.examError,
    })
    .required()
    .label("Exam"),
});

function ScribeSearchScreen({ navigation }) {
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

  const { data: languageData, request: loadLanguages } = useApi(
    languagesApi.getLanguages
  );

  const languages = languageData
    ? languageData.map(({ id, name }) => ({
        label: name,
        value: id.toString(),
      }))
    : [];

  const { data: examData, request: loadExams } = useApi(examsApi.getExams);

  const exams = examData
    ? examData.map(({ id, name }) => ({
        label: name,
        value: id.toString(),
      }))
    : [];

  useEffect(() => {
    loadLanguages();
    loadExams();
  }, []);

  const handleSubmit = async (searchCriteria, { resetForm }) => {
    const result = await scribesApi.find(searchCriteria);

    console.log(result);

    if (result.data.length == 0) {
      return alert(strings.search.noScribeFound);
    }
    if (!result.ok) {
      return alert(strings.common.serverError);
    }
    navigation.navigate(routes.SCRIBE_SEARCH_RESULTS, {
      data: result.data,
      requestUpdate: {
        candidateId: 1,
        startTime: result.config.params.from,
        endTime: result.config.params.to,
        language: searchCriteria.language,
        exam: searchCriteria.exam,
        address: searchCriteria.address,
      },
    });
  };

  return (
    <Screen style={styles.container}>
      <ScrollView>
        <AppForm
          initialValues={{
            examDate: "",
            durationHours: "",
            durationMinutes: "",
            language: "",
            exam: "",
          }}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          <AppErrorMessage error={error} visible={error} />

          <View style={styles.durations}>
            <AppFormDateTimePicker
              icon="calendar"
              name="examDate"
              mode="date"
              placeholder="Exam Date"
              editable={false}
            />
          </View>

          <View style={styles.durations}>
            <AppFormPickerField
              icon="timer-sand"
              name="durationHours"
              items={hours}
              placeholder={{
                label: "Hours",
                value: "hours",
              }}
              suffix="hr"
            />

            <AppFormPickerField
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
    flex: 1,
    justifyContent: "space-evenly",
  },
});

export default ScribeSearchScreen;
