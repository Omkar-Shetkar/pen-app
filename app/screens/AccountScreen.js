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
  name: Yup.string().required().label("Name"),
  gender: Yup.string().required().label("Gender"),
  qualification: Yup.string().required().label("Qualification"),
  mobile: Yup.number().required().label("Mobile"),
  email: Yup.string().required().label("Email"),
  availability: Yup.string().required().label("Availability"),
});

function AccountScreen(props) {
  const [error, setError] = useState();

  const user = {
    name: "user-1",
    gender: "M",
    qualification: "postgraduate",
    mobile: "9911223344",
    email: "abc@example.com",
    availability: "weekends",
  };

  const genders = [
    { label: "Male", value: "M" },
    { label: "Female", value: "F" },
    { label: "Others", value: "others" },
  ];

  const qualifications = [
    { label: "Postgraduate", value: "postgraduate" },
    { label: "Graduate", value: "graduate" },
    { label: "Intermediate", value: "intermediate" },
  ];

  const availabilities = [
    { label: "Weekends", value: "weekends" },
    { label: "Weekdays", value: "weekdays" },
    { label: "Any Day", value: "any day" },
    { label: "Not Available", value: "not available" },
  ];

  return (
    <Screen style={styles.container}>
      <ScrollView>
        <AppForm
          initialValues={user}
          onSubmit={(values) => console.log(values)}
          validationSchema={validationSchema}
        >
          <AppErrorMessage error={error} visible={error} />
          <AppFormField
            autoCorrect={false}
            icon="account-outline"
            name="name"
            placeholder="Name"
          />
          <AppFormPickerField
            icon="gender-male-female"
            name="gender"
            items={genders}
            placeholder={{
              label: "Gender",
              value: "gender",
            }}
          />
          <AppFormPickerField
            icon="school-outline"
            name="qualification"
            items={qualifications}
            placeholder={{
              label: "Qualification",
              value: "qualification",
            }}
          />
          <AppFormField
            autoCorrect={false}
            icon="cellphone"
            name="mobile"
            placeholder="Mobile"
            editable={false}
          />
          <AppFormField
            autoCorrect={false}
            icon="email-outline"
            keyboardType="email-address"
            name="email"
            placeholder="Email"
          />
          <AppFormPickerField
            icon="calendar-check-outline"
            name="availability"
            items={availabilities}
            placeholder={{
              label: "Availability",
              value: "availability",
            }}
          />
          <SubmitButton title="Update" />
        </AppForm>
      </ScrollView>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AccountScreen;
