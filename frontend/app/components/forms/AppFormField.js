import { useFormikContext } from "formik";
import React from "react";
import { StyleSheet } from "react-native";
import AppTextInput from "../AppTextInput";
import AppErrorMessage from "./AppErrorMessage";

function AppFormField({ name, width, ...otherProps }) {
  const { setFieldTouched, handleChange, errors, touched, values } =
    useFormikContext();

  console.log("values --> ", values);
  return (
    <>
      <AppTextInput
        onBlur={() => setFieldTouched(name)}
        onChangeText={handleChange(name)}
        style={styles.container}
        value={values[name]}
        width={width}
        {...otherProps}
      ></AppTextInput>

      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
});

export default AppFormField;
