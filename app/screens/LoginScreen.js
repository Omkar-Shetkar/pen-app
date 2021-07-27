import React, { useState } from "react";
import { StyleSheet, Image, TextInput, Text } from "react-native";
import * as Yup from "yup";
import AppForm from "../components/forms/AppForm";
import AppFormField from "../components/forms/AppFormField";
import SubmitButton from "../components/forms/SubmitButton";
import AppErrorMessage from "../components/forms/AppErrorMessage";
import ActivityIndicator from "../components/ActivityIndicator";
import Screen from "../components/Screen";
import authApi from "../api/auth";
import useAuth from "../auth/useAuth";
import strings from "../config/strings";

const validationSchema = Yup.object().shape({
  mobile: Yup.number().required("Mobile number is required").label("Mobile"),
  password: Yup.string()
    .required("Password is required")
    .min(4)
    .label("Password"),
});

function LoginScreen(props) {
  const [loading, setLoading] = useState(false);

  const [loginFailed, setLoginFailed] = useState(false);

  const { login } = useAuth();

  const handleSubmit = async ({ mobile, password }) => {
    setLoading(true);

    const result = await authApi.login({
      username: mobile,
      password: password,
    });

    if (!result.ok) {
      setLoading(false);
      return setLoginFailed(true);
    }
    setLoginFailed(false);
    login(result.data.access_token);
    setLoading(false);
  };

  return (
    <>
      <ActivityIndicator visible={loading} />

      <Screen style={styles.container}>
        <Image
          source={require("../assets/logo.png")}
          style={styles.logo}
        ></Image>
        {/* TODO: To be removed */}
        <AppForm
          initialValues={{ mobile: "111222", password: "1234" }}
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
            keyboardType="phone-pad"
            name="mobile"
            placeholder="Mobile"
            textContentType="telephoneNumber"
            accessible={true}
            accessibilityHint={strings.login.a11y.mobile}
          />
          <AppFormField
            autoCapitalize="none"
            autoCorrect={false}
            icon="lock"
            name="password"
            placeholder="Password"
            secureTextEntry
            textContentType="password"
            accessible={true}
            accessibilityHint={strings.login.a11y.password}
          />
          <SubmitButton
            title="Login"
            accessible={true}
            accessibilityLabel={strings.login.a11y.login}
          />
        </AppForm>
      </Screen>
    </>
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
    marginTop: 15,
    marginBottom: 20,
  },
});

export default LoginScreen;
