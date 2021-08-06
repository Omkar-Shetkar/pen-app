import { Formik } from "formik";
import React from "react";
import { View, StyleSheet } from "react-native";

function AppForm({ initialValues, onSubmit, validationSchema, children }) {
  return (
    <Formik
      initialValues={initialValues}
      onSubmit={onSubmit}
      validationSchema={validationSchema}
      enableReinitialize
    >
      {() => <>{children}</>}
    </Formik>
  );
}

export default AppForm;
