import React, { useState } from "react";
import { StyleSheet, Image, TextInput, Text } from "react-native";
import * as Yup from "yup";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import SubmitButton from "../components/forms/SubmitButton";
import Screen from "../components/Screen";
import authApi from "../api/auth";
import useAuth from "../auth/useAuth";

const validationSchema = Yup.object().shape({
  email: Yup.string().required().email().label("Email"),
  password: Yup.string().required().min(4).label("Password"),
});

function LoginScreen(props) {
  const [loginFailed, setLoginFailed] = useState(false);

  const { login } = useAuth();

  const handleSubmit = async ({ email, password }) => {
    const result = await authApi.login({ username: email, password: password });
    if (!result.ok) return setLoginFailed(true);
    setLoginFailed(false);
    login(result.data.access_token);
  };

  return (
    <Screen style={styles.container}>
      <Image source={require("../assets/logo.png")} style={styles.logo}></Image>
      {/* TODO: To be removed */}
      <AppForm
        initialValues={{ email: "omkarshetkar@gmail.com", password: "1234" }}
        onSubmit={handleSubmit}
        validationSchema={validationSchema}
      >
        <AppFormField
          autoCapitalize="none"
          autoCorrect={false}
          icon="email"
          keyboardType="email-address"
          name="email"
          placeholder="Email"
          textContentType="emailAddress"
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
