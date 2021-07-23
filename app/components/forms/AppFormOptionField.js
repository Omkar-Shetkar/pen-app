import React from "react";
import { useFormikContext } from "formik";
import RadioButtonRN from "radio-buttons-react-native";
import { StyleSheet } from "react-native";

import AppErrorMessage from "./AppErrorMessage";

function AppFormOptionField({ name, data, onSelection, ...otherProps }) {
  const { setFieldTouched, handleChange, errors, touched, values } =
    useFormikContext();

  console.log("values ", values);
  const handleUserSelection = (e) => {
    if (e) {
      onSelection(e);
      values[name] = e.id;
    }
  };

  return (
    <>
      <RadioButtonRN
        name="userTypeData"
        data={data}
        animationTypes={["pulse"]}
        selectedBtn={handleUserSelection}
        initial={values[name] ? values[name] : -1}
      />
      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default AppFormOptionField;
