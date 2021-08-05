import { useFormikContext } from "formik";
import React from "react";
import { View, StyleSheet } from "react-native";
import MultiSelect from "react-native-multiple-select";
import colors from "../../config/colors";
import AppErrorMessage from "./AppErrorMessage";

function AppFormMultiSelectField({
  uniqueKey,
  name,
  items,
  selectText,
  searchInputPlaceholderText,
  isSingle = false,
}) {
  const { setFieldValue, values, errors, touched } = useFormikContext();
  return (
    <>
      <View style={styles.container}>
        <MultiSelect
          items={items}
          uniqueKey={uniqueKey}
          onSelectedItemsChange={(selectedItems) => {
            setFieldValue(name, selectedItems);
          }}
          selectedItems={values[name]}
          selectText={selectText}
          searchInputPlaceholderText={searchInputPlaceholderText}
          submitButtonColor={colors.primary}
          single={isSingle}
          fixedHeight={false}
        />
      </View>

      <AppErrorMessage error={errors[name]} visible={touched[name]} />
    </>
  );
}

const styles = StyleSheet.create({
  container: {
    backgroundColor: colors.light,
    // overflow: "visible",
    borderRadius: 25,
    flex: 1,
    marginVertical: 10,
    paddingLeft: 10,
  },
});

export default AppFormMultiSelectField;
