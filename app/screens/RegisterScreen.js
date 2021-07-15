import React, { useEffect, useState } from "react";
import { ScrollView, StyleSheet } from "react-native";
import * as Yup from "yup";
import Toast from "react-native-toast-message";

import AppErrorMessage from "../components/forms/AppErrorMessage";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import SubmitButton from "../components/forms/SubmitButton";
import Screen from "../components/Screen";
import AppFormMultiSelectField from "../components/forms/AppFormMultiSelectField";
import authApi from "../api/auth";
import useApi from "../hooks/useApi";
import languagesApi from "../api/languages";
import gender from "../config/gender";
import userType from "../config/userType";
import useAuth from "../auth/useAuth";
import strings from "../config/strings";
import AppFormSelectionField from "../components/forms/AppFormSelectionField";

const validationSchema = Yup.object().shape({
  selectedUserType: Yup.string().required().label("User Type"),
  firstName: Yup.string().required().label("First Name"),
  lastName: Yup.string().required().label("Last Name"),
  mobile: Yup.number().required().label("Mobile"),
  email: Yup.string().optional().email().label("Email"),
  password: Yup.string().required().min(4).label("Password"),
  gender: Yup.string().required().label("Gender"),
  languages: Yup.array().required().min(1).label("Languages"),
  // availability: Yup.string().required().label("Availability"),
  location: Yup.string().required().label("Location"),
});

function RegisterScreen() {
  const [failed, setFailed] = useState(false);
  const [isScribe, setIsScribe] = useState(false);

  const { login } = useAuth();

  const userTypes = [
    { id: "1", label: "Scribe", value: userType.SCRIBE },
    { id: "2", label: "Candidate", value: userType.CANDIDATE },
  ];

  const genders = [
    { label: "Male", value: gender.MALE },
    { label: "Female", value: gender.FEMALE },
    { label: "Others", value: gender.OTHER },
  ];

  const cities = [
    {
      id: "1",
      name: "Bangalore",
    },
    { id: "2", name: "Mysore" },
    { id: "3", name: "Bidar" },
    { id: "4", name: "Kalburgi" },
    { id: "5", name: "Hyderabad" },
  ];

  const { data: languages, request: loadLanguages } = useApi(
    languagesApi.getLanguages
  );

  // const availabilities = [
  //   { label: "Weekends", value: "weekends" },
  //   { label: "Weekdays", value: "weekdays" },
  //   { label: "Any Day", value: "any day" },
  //   { label: "Not Available", value: "not available" },
  // ];

  useEffect(() => {
    loadLanguages();
  }, []);

  const onUserSelection = (e) => {
    setIsScribe(false);
    let message = strings.register.candidate;

    if (e.value === userType.SCRIBE) {
      setIsScribe(true);
      message = strings.register.scribe;
    }

    Toast.show({
      type: "info",
      text2: message,
      visibilityTime: 4000,
    });
  };

  const handleSubmit = async ({
    selectedUserType,
    firstName,
    lastName,
    email,
    mobile,
    password,
    gender,
    languages,
    // availability,
    location,
  }) => {
    const result = await authApi.register({
      userType: selectedUserType,
      firstName,
      lastName,
      email,
      credentials: { username: mobile, password },
      mobile,
      gender,
      languages,
      // availability,
      location,
    });
    if (result.ok) {
      loginFun({ mobile, password });
    }
    if (!result.ok) return setFailed(true);
  };

  const loginFun = async ({ mobile, password }) => {
    console.log("mobile ", mobile);
    console.log("password ", password);
    const result = await authApi.login({
      username: mobile,
      password: password,
    });
    console.log("login ", result);
    if (!result.ok) return setFailed(true);
    setFailed(false);
    login(result.data.access_token);
  };

  return (
    <Screen style={styles.container}>
      <ScrollView>
        {/* TODO: To be removed */}
        <AppForm
          initialValues={{
            selectedUserType: "",
            firstName: "Vedant",
            lastName: "Shetkar",
            mobile: "1122334455",
            email: "vedant.shetkar@gmail.com",
            password: "1234",
            gender: "MALE",
            languages: [1],
            // availability: "weekends",
            location: [1],
          }}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
        >
          <AppErrorMessage error="Registration failed!" visible={failed} />

          <AppFormSelectionField
            name="selectedUserType"
            data={userTypes}
            onSelection={onUserSelection}
          />

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
              <AppFormMultiSelectField
                uniqueKey="id"
                name="languages"
                items={languages}
                selectText="Pick Languages"
                searchInputPlaceholderText="Search Languages..."
              />
              {/* <AppFormPickerField
                icon="calendar-check-outline"
                name="availability"
                items={availabilities}
                placeholder={{
                  label: "Availability",
                  value: "",
                }}
              /> */}
            </>
          )}

          <AppFormMultiSelectField
            uniqueKey="id"
            name="location"
            items={cities}
            selectText="Location"
            searchInputPlaceholderText="Search City..."
            isSingle={true}
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
