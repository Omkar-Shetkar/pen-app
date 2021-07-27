import React, { useEffect, useState } from "react";
import { ScrollView, StyleSheet } from "react-native";
import * as Yup from "yup";
import Toast from "react-native-toast-message";

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
import AppFormOptionField from "../components/forms/AppFormOptionField";

const mobileRegExp =
  /^((\+[1-9]{1,4}[ -]?)|(\([0-9]{2,3}\)[ -]?)|([0-9]{2,4})[ -]?)*?[0-9]{3,4}[ -]?[0-9]{3,4}$/;

const validationSchema = Yup.object().shape({
  selectedUserType: Yup.string().required().label("User Type"),
  firstName: Yup.string().required().label("First Name"),
  lastName: Yup.string().required().label("Last Name"),
  mobile: Yup.string()
    .required()
    .matches(mobileRegExp, "Mobile number not valid")
    .label("Mobile"),
  email: Yup.string().optional().email().label("Email"),
  password: Yup.string().required().min(4).label("Password"),
  gender: Yup.string().required().label("Gender"),
  languages: Yup.array()
    .label("Languages")
    .when("selectedUserType", {
      is: "1",
      then: Yup.array().min(1).required().label("Languages"),
      otherwise: Yup.array().notRequired(),
    }),
  city: Yup.string()
    .label("City")
    .when("selectedUserType", {
      is: "1",
      then: Yup.string().required().label("City"),
      otherwise: Yup.string().notRequired(),
    }),
});

function RegisterScreen() {
  const [loading, setLoading] = useState(false);
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
    city,
  }) => {
    setLoading(true);

    let selectUserTypeValue = userTypes.find(
      (u) => u.id === selectedUserType
    ).value;

    const result = await authApi.register({
      userType: selectUserTypeValue,
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
          {/* TODO: To be removed */}
          <AppForm
            // initialValues={{
            //   selectedUserType: "",
            //   firstName: "Omkar",
            //   lastName: "S",
            //   mobile: "9844491285",
            //   email: "omkar.shetkar@gmail.com",
            //   password: "1234",
            //   gender: "MALE",
            //   // languages: [languages[1]],
            // }}
            initialValues={{
              selectedUserType: "",
              firstName: "Vedant",
              lastName: "S",
              mobile: "1122334455",
              email: "vedant.shetkar@gmail.com",
              password: "1234",
              gender: "MALE",
              languages: [languages.length > 0 ? languages[1].id : ""],
              // availability: "weekends",
              city: cities.length > 0 ? cities[1].value : "",
            }}
            // initialValues={{}}
            onSubmit={handleSubmit}
            validationSchema={validationSchema}
          >
            <AppErrorMessage error="Registration failed!" visible={failed} />

            <AppFormOptionField
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
});

export default RegisterScreen;
