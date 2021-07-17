import React, { useContext, useEffect, useState } from "react";
import { ScrollView, StyleSheet } from "react-native";
import * as Yup from "yup";

import userApi from "../api/user";
import languagesApi from "../api/languages";
import locationApi from "../api/location";
import AuthContext from "../auth/context";
import AppButton from "../components/AppButton";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import AppFormMultiSelectField from "../components/forms/AppFormMultiSelectField";
import SubmitButton from "../components/forms/SubmitButton";
import Screen from "../components/Screen";
import gender from "../config/gender";
import userType from "../config/userType";

const validationSchema = Yup.object().shape({
  firstName: Yup.string().required().label("Name"),
  gender: Yup.string().required().label("Gender"),
  qualification: Yup.string().required().label("Qualification"),
  mobile: Yup.number().required().label("Mobile"),
  email: Yup.string().required().label("Email"),
  availability: Yup.string().required().label("Availability"),
});

function AccountScreen(props) {
  const [error, setError] = useState();

  const { user, setUser } = useContext(AuthContext);

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

  console.log("account screen userDetails", userDetails);

  const genders = [
    { label: "Male", value: gender.MALE },
    { label: "Female", value: gender.FEMALE },
    { label: "Others", value: gender.OTHER },
  ];

  return (
    <Screen style={styles.container}>
      <ScrollView>
        <AppForm
          initialValues={userDetails}
          onSubmit={(values) => console.log(values)}
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
          <AppFormPickerField
            icon="gender-male-female"
            name="gender"
            items={genders}
            placeholder={{
              label: "Gender",
              value: "",
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

          {userDetails.userType === userType.SCRIBE && (
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

          <SubmitButton title="Update" />
        </AppForm>
        {/* TODO: Logout to be implemented */}
        <AppButton
          title="Logout"
          color="secondary"
          onPress={() => setUser(null)}
        />
      </ScrollView>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AccountScreen;
