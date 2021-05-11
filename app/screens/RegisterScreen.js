import React, { useState } from "react";
import { View, StyleSheet } from "react-native";
import { ScrollView } from "react-native-gesture-handler";
import * as Yup from "yup";
import MultiSelect from "react-native-multiple-select";

import AppErrorMessage from "../components/forms/AppErrorMessage";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import AppFormPickerField from "../components/forms/AppFormPickerField";
import SubmitButton from "../components/forms/SubmitButton";
import Screen from "../components/Screen";
import colors from "../config/colors";
import defaultStyles from "../config/styles";

import AppFormMultiSelectField from "../components/forms/AppFormMultiSelectField";

const validationSchema = Yup.object().shape({
  name: Yup.string().required().label("Name"),
  email: Yup.string().required().email().label("Email"),
  password: Yup.string().required().min(4).label("Password"),
});

function RegisterScreen({ isScribe }) {
  const [error, setError] = useState();
  const [selectedItems, setSelectedItems] = useState();

  const qualifications = [
    { label: "Postgraduate", value: "postgraduate" },
    { label: "Graduate", value: "graduate" },
    { label: "Intermediate", value: "intermediate" },
  ];

  const languages = [
    {
      id: 1,
      name: "Kannada",
    },
    {
      id: 2,
      name: "English",
    },
    {
      id: 3,
      name: "Hindi",
    },
    {
      id: 4,
      name: "Telugu",
    },
  ];
  isScribe = true;

  const onSelectedItemsChange = (selectedItems) => {
    setSelectedItems(selectedItems);
  };

  return (
    <Screen style={styles.container}>
      <ScrollView>
        <AppForm
          initialValues={{ name: "", email: "", password: "" }}
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
          {isScribe && (
            <AppFormPickerField
              icon="school-outline"
              name="qualification"
              items={qualifications}
              placeholder={{
                label: "Qualification",
                value: "qualification",
              }}
            />
          )}
          <AppFormMultiSelectField
            uniqueKey="id"
            name="languages"
            items={languages}
            selectText="Pick Languages"
            searchInputPlaceholderText="Search Languages..."
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
