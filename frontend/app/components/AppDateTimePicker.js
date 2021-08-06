import React, { useEffect, useState } from "react";
import { View, StyleSheet, Modal } from "react-native";
import DateTimePicker from "@react-native-community/datetimepicker";

import defaultStyles from "../config/styles";
import AppText from "./AppText";
import AppButton from "./AppButton";

function AppDateTimePicker({ icon, width = "100%" }) {
  const [date, setDate] = useState(new Date());
  const [mode, setMode] = useState("date");
  const [show, setShow] = useState(true);
  const [showPicker, setShowPicker] = useState(true);

  const onChange = (event, selectedDate) => {
    const currentDate = selectedDate || date;
    setShowPicker(Platform.OS === "ios");
    setDate(currentDate);
  };

  const showMode = (currentMode) => {
    setShowPicker(true);
    setMode(currentMode);
  };

  const showDatepicker = () => {
    showMode("date");
  };

  const showTimepicker = () => {
    showMode("time");
  };

  const time = () => {
    const hours = date.getHours() % 12;
    const minutes = date.getMinutes();
    const format = date.getHours() >= 12 ? "PM" : "AM";
    return (
      (hours ? hours : 12) +
      ":" +
      (minutes < 10 ? "0" + minutes : minutes) +
      " " +
      format
    );
  };

  const handleCancel = () => {
    setShow(false);
  };

  return (
    <Modal
      visible={show}
      onRequestClose={() => {
        setShow(!show);
      }}
    >
      <View style={styles.container}>
        <AppText>Select Date</AppText>
        <AppText onPress={showDatepicker} style={styles.text}>
          {date.toDateString()}
        </AppText>
        <AppText>Select Time</AppText>
        <AppText onPress={showTimepicker} style={styles.text}>
          {/* {date.getHours() + ":" + date.getMinutes() + date.toTimeString()} */}
          {time()}
        </AppText>
        {showPicker && (
          <DateTimePicker
            testID="dateTimePicker"
            value={date}
            mode={mode}
            is24Hour={false}
            display="default"
            onChange={onChange}
          />
        )}
        <View style={styles.buttons}>
          <AppButton title="Cancel" color="secondary" onPress={handleCancel} />
          <AppButton title="Select" color="medium" />
        </View>
      </View>
    </Modal>
  );
}

const styles = StyleSheet.create({
  buttons: {
    alignItems: "center",
    flexDirection: "row",
    justifyContent: "center",
    width: "50%",
  },
  container: {
    alignItems: "center",
    backgroundColor: defaultStyles.colors.white,
    borderRadius: 25,
    flexDirection: "column",
    marginVertical: 10,
    padding: 15,
  },
  text: {
    padding: 30,
    backgroundColor: defaultStyles.colors.light,
    width: "100%",
    margin: 10,
    borderRadius: 10,
  },
});

export default AppDateTimePicker;
