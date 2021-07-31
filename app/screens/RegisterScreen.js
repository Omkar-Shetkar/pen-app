import React, { useEffect, useState } from "react";
import { ScrollView, StyleSheet } from "react-native";
import * as Yup from "yup";

import ActivityIndicator from "../components/ActivityIndicator";
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
import locationApi from "../api/location";
import gender from "../config/gender";
import userType from "../config/userType";
import useAuth from "../auth/useAuth";
import strings from "../config/strings";
import AppFormRadioButtons from "../components/forms/AppFormRadioButtons";
import AppText from "../components/AppText";
import colors from "../config/colors";

const mobileRegExp =
  /^((\+[1-9]{1,4}[ -]?)|(\([0-9]{2,3}\)[ -]?)|([0-9]{2,4})[ -]?)*?[0-9]{3,4}[ -]?[0-9]{3,4}$/;

const validationSchema = Yup.object().shape({
  selectedUserType: Yup.string().required().label("User Type"),
  firstName: Yup.string().required().label("First Name"),
  lastName: Yup.string().required().label("Last Name"),
  mobile: Yup.string()
    .required()
    .max(10)
    .matches(mobileRegExp, "Mobile number not valid")
    .label("Mobile"),
  email: Yup.string().optional().email().label("Email"),
  password: Yup.string().required().min(4).label("Password"),
  gender: Yup.string().required().label("Gender"),
  languages: Yup.array()
    .label("Languages")
    .when("selectedUserType", {
      is: "SCRIBE",
      then: Yup.array().min(1).required().label("Languages"),
      otherwise: Yup.array().notRequired(),
    }),
  city: Yup.string()
    .label("City")
    .when("selectedUserType", {
      is: "SCRIBE",
      then: Yup.string().required().label("City"),
      otherwise: Yup.string().notRequired(),
    }),
});

function RegisterScreen() {
  const [loading, setLoading] = useState(false);
  const [failed, setFailed] = useState(false);
  const [isScribe, setIsScribe] = useState(false);

  const { login } = useAuth();
  let registrationDetails = {};

  const userTypes = [
    {
      label: "Scribe",
      value: userType.SCRIBE,
      description: strings.register.scribe,
    },
    {
      label: "Candidate",
      value: userType.CANDIDATE,
      description: strings.register.candidate,
    },
  ];

  const genders = [
    { label: "Male", value: gender.MALE },
    { label: "Female", value: gender.FEMALE },
    { label: "Others", value: gender.OTHER },
  ];

  const { data: languages, request: loadLanguages } = useApi(
    languagesApi.getLanguages
  );

  const { data: cityData, request: loadCities } = useApi(locationApi.getCities);

  const cities = cityData
    ? cityData.map(({ id, name }) => ({
        label: name,
        value: id.toString(),
      }))
    : [];

  useEffect(() => {
    loadLanguages();
    loadCities();
  }, []);

  const onUserSelection = (e) => {
    const current = e.value === userType.SCRIBE ? true : false;

    if (isScribe !== current) {
      registrationDetails = {};
    }
    setIsScribe(current);
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
    city,
  }) => {
    setLoading(true);

    const result = await authApi.register({
      userType: selectedUserType,
      firstName,
      lastName,
      email,
      credentials: { username: mobile, password },
      mobile,
      gender,
      languages,
      city,
    });
    if (result.ok) {
      loginFun({ mobile, password });
    }

    setLoading(false);
    if (!result.ok) return setFailed(true);
  };

  const loginFun = async ({ mobile, password }) => {
    const result = await authApi.login({
      username: mobile,
      password: password,
    });
    if (!result.ok) return setFailed(true);
    setFailed(false);
    login(result.data.access_token);
  };

  return (
    <>
      <ActivityIndicator visible={loading} />
      <Screen style={styles.container}>
        <ScrollView>
          <AppForm
            initialValues={registrationDetails}
            onSubmit={handleSubmit}
            validationSchema={validationSchema}
          >
            <AppErrorMessage error="Registration failed!" visible={failed} />

            <AppText style={styles.label}>{strings.register.userType}</AppText>
            <AppFormRadioButtons
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
              placeholder="Password (at least 4 chars)"
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

                <AppFormPickerField
                  icon="map-marker-outline"
                  name="city"
                  items={cities}
                  placeholder={{
                    label: "City",
                    value: "",
                  }}
                />
              </>
            )}

            <SubmitButton title="Register" />
          </AppForm>
        </ScrollView>
      </Screen>
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
  label: {
    color: colors.medium,
    fontWeight: "400",
  },
});

export default RegisterScreen;
