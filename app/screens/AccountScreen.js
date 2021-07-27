import React, { useContext, useEffect, useState } from "react";
import { ScrollView, StyleSheet } from "react-native";
import * as Yup from "yup";
import Toast from "react-native-toast-message";

import languagesApi from "../api/languages";
import locationApi from "../api/location";
import userApi from "../api/user";
import AuthContext from "../auth/context";
import AppButton from "../components/AppButton";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppFormMultiSelectField from "../components/forms/AppFormMultiSelectField";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import ActivityIndicator from "../components/ActivityIndicator";
import SubmitButton from "../components/forms/SubmitButton";
import Screen from "../components/Screen";
import gender from "../config/gender";
import userTypeData from "../config/userType";
import useAuth from "../auth/useAuth";
import strings from "../config/strings";

const validationSchema = Yup.object().shape({
  userType: Yup.string().required().label("User Type"),
  firstName: Yup.string().required().label("First Name"),
  lastName: Yup.string().required().label("Last Name"),
  mobile: Yup.number().required().label("Mobile"),
  email: Yup.string().optional().email().label("Email"),
  gender: Yup.string().required().label("Gender"),
  //TODO Fix the validation for languages and city
  languages: Yup.array().when("userType", {
    is: (val) => val === "SCRIBE",
    then: Yup.array().min(1).required().label("Languages"),
    otherwise: Yup.array().notRequired(),
  }),
  city: Yup.string().when("userType", {
    is: (val) => val === "SCRIBE",
    then: Yup.string().required().label("City"),
    otherwise: Yup.string().notRequired(),
  }),
});

function AccountScreen(props) {
  const [loading, setLoading] = useState(false);

  const [error, setError] = useState();

  const { user } = useContext(AuthContext);

  const { logout } = useAuth();

  const { data: userDetails, request: loadUserDetails } = useApi(
    userApi.getUserDetails
  );

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
    loadUserDetails(user.sub);
    loadLanguages();
    loadCities();
  }, []);

  const genders = [
    { label: "Male", value: gender.MALE },
    { label: "Female", value: gender.FEMALE },
    { label: "Others", value: gender.OTHER },
  ];

  const { error: updateError, request: updateUser } = useApi(
    userApi.updateUserDetails
  );

  const handleSubmit = async ({
    firstName,
    lastName,
    email,
    mobile,
    gender,
    languages,
    city,
  }) => {
    setLoading(true);

    userDetails.firstName = firstName;
    userDetails.lastName = lastName;
    userDetails.email = email;
    userDetails.mobile = mobile;
    userDetails.gender = gender;
    if (userDetails.userType === userTypeData.SCRIBE) {
      userDetails.languages = languages;
      userDetails.city = city;
    }
    updateUser(user.sub, userDetails);

    setLoading(false);

    if (updateError) {
      Toast.show({
        type: "error",
        text1: strings.account.failed,
        visibilityTime: 4000,
      });
    } else {
      Toast.show({
        type: "success",
        text1: strings.account.success,
        visibilityTime: 4000,
      });
    }
  };

  return (
    <>
      <ActivityIndicator visible={loading} />

      <Screen style={styles.container}>
        <ScrollView>
          <AppForm
            initialValues={userDetails}
            onSubmit={handleSubmit}
            validationSchema={validationSchema}
          >
            <AppErrorMessage error={error} visible={error} />
            <AppFormField
              autoCorrect={false}
              icon="account-circle"
              name="userType"
              placeholder="User Type"
              editable={false}
            />
            <AppFormField
              autoCorrect={false}
              icon="account-outline"
              name="firstName"
              placeholder="First Name"
              editable={false}
            />
            <AppFormField
              autoCorrect={false}
              icon="account-outline"
              name="lastName"
              placeholder="Last Name"
              editable={false}
            />
            <AppFormPickerField
              icon="gender-male-female"
              name="gender"
              items={genders}
              placeholder={{
                label: "Gender",
                value: "",
              }}
              disabled
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
              editable={false}
            />

            {userDetails && userDetails.userType === userTypeData.SCRIBE && (
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

            {userDetails.userType === userTypeData.SCRIBE && (
              <SubmitButton title="Update" />
            )}
          </AppForm>
          {/* TODO: Logout to be implemented */}
          <AppButton title="Logout" color="secondary" onPress={logout} />
        </ScrollView>
      </Screen>
    </>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AccountScreen;
