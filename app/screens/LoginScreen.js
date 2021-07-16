import React, { useState } from "react";
import { StyleSheet, Image, TextInput, Text } from "react-native";
import * as Yup from "yup";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import SubmitButton from "../components/forms/SubmitButton";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import Screen from "../components/Screen";
import authApi from "../api/auth";
import useAuth from "../auth/useAuth";

const validationSchema = Yup.object().shape({
  mobile: Yup.number().required().label("Mobile"),
  password: Yup.string().required().min(4).label("Password"),
});

function LoginScreen(props) {
  const [loginFailed, setLoginFailed] = useState(false);

  const { login } = useAuth();

  const handleSubmit = async ({ mobile, password }) => {
    const result = await authApi.login({
      username: mobile,
      password: password,
    });
    if (!result.ok) return setLoginFailed(true);
    setLoginFailed(false);
    login(result.data.access_token);
  };

  return (
    <Screen style={styles.container}>
      <Image source={require("../assets/logo.png")} style={styles.logo}></Image>
      {/* TODO: To be removed */}
      <AppForm
        initialValues={{ mobile: "1122334455", password: "1234" }}
        onSubmit={handleSubmit}
        validationSchema={validationSchema}
      >
        <AppErrorMessage
          error="Invalid mobile or password."
          visible={loginFailed}
        />
        <AppFormField
          autoCapitalize="none"
          autoCorrect={false}
          icon="cellphone"
          keyboardType="numeric"
          name="mobile"
          placeholder="Mobile"
          textContentType="telephoneNumber"
        ></AppFormField>
        <AppFormField
          autoCapitalize="none"
          autoCorrect={false}
          icon="lock"
          name="password"
          placeholder="Password"
          secureTextEntry
          textContentType="password"
        ></AppFormField>
        <SubmitButton title="Login" />
      </AppForm>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 10,
  },
  logo: {
    width: 80,
    height: 80,
    alignSelf: "center",
    marginTop: 50,
    marginBottom: 20,
  },
});

export default LoginScreen;
