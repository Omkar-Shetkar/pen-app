import React, { useEffect, useState } from "react";
import { ScrollView, StyleSheet, View } from "react-native";
import * as Yup from "yup";

import AppErrorMessage from "../components/forms/AppErrorMessage";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import SubmitButton from "../components/forms/SubmitButton";
import Screen from "../components/Screen";
import AppFormMultiSelectField from "../components/forms/AppFormMultiSelectField";
import authApi from "../api/auth";
import useAuth from "../auth/useAuth";
import useApi from "../hooks/useApi";
import languagesApi from "../api/languages";

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required().label("First Name"),
  lastName: Yup.string().required().label("Last Name"),
  mobile: Yup.number().required().label("Mobile"),
  email: Yup.string().optional().email().label("Email"),
  password: Yup.string().required().min(8).label("Password"),
  gender: Yup.string().required().label("Gender"),
  qualification: Yup.string().required().label("Qualification"),
  languages: Yup.array().required().min(1).label("Languages"),
  availability: Yup.string().required().label("Availability"),
  location: Yup.string().required().label("Location"),
});

function RegisterScreen({ isScribe }) {
  const [error, setError] = useState();
  const [selectedItems, setSelectedItems] = useState();

  const qualifications = [
    { label: "Postgraduate", value: "postgraduate" },
    { label: "Graduate", value: "graduate" },
    { label: "Intermediate", value: "intermediate" },
  ];

  const genders = [
    { label: "Male", value: "M" },
    { label: "Female", value: "F" },
    { label: "Others", value: "others" },
  ];

  useEffect(() => {
    console.log("useeffect called...");
    loadLanguages();
  }, []);

  const { data: languages, request: loadLanguages } = useApi(
    languagesApi.getLanguages
  );

  const availabilities = [
    { label: "Weekends", value: "weekends" },
    { label: "Weekdays", value: "weekdays" },
    { label: "Any Day", value: "any day" },
    { label: "Not Available", value: "not available" },
  ];

  isScribe = true;

  const onSelectedItemsChange = (selectedItems) => {
    setSelectedItems(selectedItems);
  };

  const handleSubmit = async ({
    firstName,
    lastName,
    mobile,
    email,
    password,
    gender,
    languages,
    availability,
    location,
  }) => {
    const result = await authApi.login({ username: email, password: password });
    console.log("result ", result);
    if (!result.ok) return setLoginFailed(true);
    setLoginFailed(false);
    login(result.data.access_token);
  };

  return (
    <Screen style={styles.container}>
      <ScrollView>
        <AppForm
          initialValues={{
            firstName: "",
            lastName: "",
            mobile: "",
            email: "",
            password: "",
            gender: "",
            qualification: "",
            languages: [],
            availability: "",
            location: "",
          }}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          <AppErrorMessage error={error} visible={error} />

          <AppFormField
            autoCorrect={false}
            icon="account-outline"
            name="firstName"
            placeholder="First Name"
          />
          <AppFormField
            autoCorrect={false}
            icon="account-outline"
            name="lastName"
            placeholder="Last Name"
          />

          <AppFormField
            autoCorrect={false}
            icon="cellphone"
            keyboardType="numeric"
            name="mobile"
            placeholder="Mobile"
            textContentType="telephoneNumber"
          />

          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="email-outline"
            keyboardType="email-address"
            name="email"
            placeholder="Email"
            textContentType="emailAddress"
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="lock-outline"
            name="password"
            placeholder="Password"
            secureTextEntry
            textContentType="password"
          />
          <AppFormPickerField
            icon="gender-male-female"
            name="gender"
            items={genders}
            placeholder={{
              label: "Gender",
              value: "",
            }}
          />
          {isScribe && (
            <>
              <AppFormPickerField
                icon="school-outline"
                name="qualification"
                items={qualifications}
                placeholder={{
                  label: "Qualification",
                  value: "",
                }}
              />
              <AppFormMultiSelectField
                uniqueKey="id"
                name="languages"
                items={languages}
                selectText="Pick Languages"
                searchInputPlaceholderText="Search Languages..."
              />
              <AppFormPickerField
                icon="calendar-check-outline"
                name="availability"
                items={availabilities}
                placeholder={{
                  label: "Availability",
                  value: "",
                }}
              />
            </>
          )}

          <AppFormField
            autoCorrect={false}
            icon="map-marker-outline"
            name="location"
            placeholder="Location"
          />
          <SubmitButton title="Register" />
        </AppForm>
      </ScrollView>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
});

export default RegisterScreen;
