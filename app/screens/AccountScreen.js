import React, { useState, useContext } from "react";
import { ScrollView, StyleSheet } from "react-native";
import * as Yup from "yup";

import Screen from "../components/Screen";
import AppButton from "../components/AppButton";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import SubmitButton from "../components/forms/SubmitButton";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import AuthContext from "../auth/context";
import gender from "../config/gender";

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
  const { user, setUser } = useContext(AuthContext);

  const genders = [
    { label: "Male", value: gender.MALE },
    { label: "Female", value: gender.FEMALE },
    { label: "Others", value: gender.OTHER },
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
            name="firstName"
            placeholder="Name"
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
