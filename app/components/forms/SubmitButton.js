import { useFormikContext } from "formik";
import React from "react";
import AppButton from "../AppButton.js";

function SubmitButton({ title }) {
  const { handleSubmit } = useFormikContext();
  return <AppButton title={title} onPress={handleSubmit}></AppButton>;
}

export default SubmitButton;
