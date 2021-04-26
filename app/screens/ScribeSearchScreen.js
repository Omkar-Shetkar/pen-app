import React, { useState } from "react";
import { StyleSheet, TouchableOpacity, View } from "react-native";

import Screen from "../components/Screen";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import SubmitButton from "../components/forms/SubmitButton";
import AppDateTimePicker from "../components/AppDateTimePicker";

function ScribeSearchScreen(props) {
  const [error, setError] = useState();
  const [from, setFrom] = useState();
  const [to, setTo] = useState();
  const [showDatePicker, setShowDatePicker] = useState(false);

  const selectFrom = () => {
    setShowDatePicker(true);
    console.log("showDatePikcer", showDatePicker);
  };

  return (
    <Screen style={styles.container}>
      <AppForm>
        <AppErrorMessage error={error} visible={error} />
        {showDatePicker && <AppDateTimePicker />}
        <TouchableOpacity onPress={selectFrom}>
          <AppFormField
            icon="timetable"
            name="from"
            placeholder="From"
            editable={false}
          />
        </TouchableOpacity>
        <AppFormField icon="timetable" name="to" placeholder="To" />
        <AppFormField
          autoCorrect={false}
          // FIXME: Use proper icon
          icon="mail"
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
