import React from "react";
import { StyleSheet } from "react-native";
import { FlatList } from "react-native-gesture-handler";
import NotificationCardComponent from "../components/NotificationCardComponent";
import Screen from "../components/Screen";

function NotificationsScreen(props) {
  const notifications = [
    { id: 1, message: "Your OTP for the exam is 8877" },
    { id: 2, message: "Reminder for your today's exam at 10.30 AM" },
  ];

  return (
    <Screen>
      <FlatList
        data={notifications}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <NotificationCardComponent message={item.message} />
        )}
      ></FlatList>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default NotificationsScreen;
