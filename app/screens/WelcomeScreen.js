import React from "react";
import { Image, ImageBackground, StyleSheet, Text, View } from "react-native";
import AppButton from "../components/AppButton";
import colors from "../config/colors";
import strings from "../config/strings";
import routes from "../navigation/routes";

export default function WelcomeScreen({ navigation }) {
  return (
    <ImageBackground
      source={require("../assets/background.jpg")}
      style={styles.background}
      blurRadius={8}
    >
      <View style={styles.logoContainer} accessible={true}>
        <Image source={require("../assets/logo.png")} style={styles.logo} />
        <Text
          style={styles.text}
          accessibilityLabel={strings.welcome.a11y.welcomeMessage}
        >
          {strings.welcome.slogan}
        </Text>
      </View>

      <AppButton
        title="Login"
        accessible={true}
        accessibilityLabel={strings.welcome.a11y.login}
        backgroundColor={colors.primary}
        onPress={() => navigation.navigate(routes.LOGIN)}
      />
      <AppButton
        title="Register"
        accessible={true}
        accessibilityLabel={strings.welcome.a11y.register}
        backgroundColor={colors.secondary}
        onPress={() => navigation.navigate(routes.REGISTER)}
      />
    </ImageBackground>
  );
}

const styles = StyleSheet.create({
  background: {
    flex: 1,
    justifyContent: "flex-end",
    alignItems: "center",
  },
  loginButton: {
    width: "100%",
    height: 70,
    backgroundColor: "#fc5c65",
  },
  logoContainer: {
    position: "absolute",
    top: "10%",
    alignItems: "center",
    width: "100%",
  },
  logo: {
    width: 125,
    height: 100,
  },
  registerButton: {
    width: "100%",
    height: 70,
    backgroundColor: "#4ecdc4",
  },
  text: {
    color: colors.light,
    padding: 10,
    fontStyle: "italic",
    fontWeight: "600",
    fontSize: 18,
  },
});
