import { useFormikContext } from "formik";
import React from "react";
import AppButton from "../AppButton.js";

function SubmitButton({ title, ...otherProperties }) {
  const { handleSubmit } = useFormikContext();
  return (
    <AppButton
      title={title}
      onPress={handleSubmit}
      {...otherProperties}
    ></AppButton>
  );
}

export default SubmitButton;
