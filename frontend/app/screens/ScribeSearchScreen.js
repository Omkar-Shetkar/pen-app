import React, { useContext, useEffect, useState } from "react";
import { ScrollView, StyleSheet, Text, View } from "react-native";
import * as Yup from "yup";

import ActivityIndicator from "../components/ActivityIndicator";
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
import locationApi from "../api/location";
import strings from "../config/strings";
import routes from "../navigation/routes";
import AuthContext from "../auth/context";
import AppText from "../components/AppText";
import defaultStyles from "../config/styles";

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
  address: Yup.string().required().max(100),
  city: Yup.string()
    .matches(/\b(?!city\b)\w+/, {
      message: strings.search.cityError,
    })
    .required()
    .label("City"),
});

function ScribeSearchScreen({ navigation }) {
  const { user, setUser } = useContext(AuthContext);

  const [loading, setLoading] = useState(false);

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

  const { data: cityData, request: loadCities } = useApi(locationApi.getCities);

  const cities = cityData
    ? cityData.map(({ id, name }) => ({
        label: name,
        value: id.toString(),
      }))
    : [];

  useEffect(() => {
    loadLanguages();
    loadExams();
    loadCities();
  }, []);

  const handleSubmit = async (searchCriteria, { resetForm }) => {
    setLoading(true);
    const result = await scribesApi.find({
      candidate: user.sub,
      ...searchCriteria,
    });
    setLoading(false);

    if (result.data.length == 0) {
      return alert(strings.search.noScribeFound);
    }
    if (!result.ok) {
      return alert(strings.common.serverError);
    }
    navigation.navigate(routes.SCRIBE_SEARCH_RESULTS, {
      data: result.data,
      requestUpdate: {
        candidateId: user.sub,
        startTime: result.config.params.from,
        endTime: result.config.params.to,
        language: searchCriteria.language,
        exam: searchCriteria.exam,
        address: searchCriteria.address,
        city: searchCriteria.city,
      },
    });
  };

  return (
    <>
      <ActivityIndicator visible={loading} />
      <Screen style={styles.container}>
        <ScrollView>
          <AppForm
            initialValues={{
              examDate: "",
              durationHours: "",
              durationMinutes: "",
              language: "",
              exam: "",
              address: "",
              city: "",
            }}
            onSubmit={handleSubmit}
            validationSchema={validationSchema}
          >
            <AppErrorMessage error={error} visible={error} />

            <View
              style={styles.durations}
              accessible={true}
              accessibilityLabel={strings.search.a11y.examDateTime}
            >
              <AppFormDateTimePicker
                icon="calendar"
                name="examDate"
                mode="date"
                placeholder="Exam Date"
                editable={false}
              />
            </View>

            <AppText value="Exam Duration" style={styles.label}>
              Exam Duration
            </AppText>
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
                accessible={true}
                accessibilityLabel={strings.search.a11y.examDurationHr}
              />

              <AppFormPickerField
                name="durationMinutes"
                items={minutes}
                placeholder={{
                  label: "Minutes",
                  value: "minutes",
                }}
                suffix="min"
                accessible={true}
                accessibilityLabel={strings.search.a11y.examDurationMin}
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
              accessible={true}
              accessibilityLabel={strings.search.a11y.examLanguage}
            />

            <AppFormPickerField
              icon="tournament"
              name="exam"
              items={exams}
              placeholder={{
                label: "Exam",
                value: "exam",
              }}
              accessible={true}
              accessibilityLabel={strings.search.a11y.examName}
            />
            <AppFormField
              autoCorrect={false}
              icon="map-marker-outline"
              name="address"
              placeholder="Address"
              accessible={true}
              accessibilityLabel={strings.search.a11y.examAddress}
            />
            <AppFormPickerField
              icon="map-marker-outline"
              name="city"
              items={cities}
              placeholder={{
                label: "City",
                value: "",
              }}
              accessible={true}
              accessibilityLabel={strings.search.a11y.examCity}
            />
            <SubmitButton title="Search" />
          </AppForm>
        </ScrollView>
      </Screen>
    </>
  );
}

const styles = StyleSheet.create({
  container: {},
  durations: {
    flexDirection: "row",
    flex: 1,
    justifyContent: "space-evenly",
  },
  label: {
    paddingLeft: 10,
    fontSize: 14,
    color: defaultStyles.colors.medium,
  },
});

export default ScribeSearchScreen;
