import React from "react";
import { StyleSheet } from "react-native";
import { FlatList } from "react-native-gesture-handler";
import RequestCardComponent from "../components/RequestCardComponent";

import Screen from "../components/Screen";
import colors from "../config/colors";

function CandidateAppliedScreen(props) {
  const listing = [
    {
      id: 1,
      name: "scribe-1",
      gender: "M",
      qualification: "SSLC",
      exam: "SSLC",
      examDate: "01-06-2021 10:30",
      duration: "2 Hr 30 Min",
      language: "English",
      venue: "Address-1",
    },
    {
      id: 2,
      name: "scribe-2",
      gender: "F",
      qualification: "MCA",
      exam: "PUC",
      examDate: "02-06-2021 10:30",
      duration: "3 Hr 30 Min",
      language: "Kannada",
      venue: "Address-2",
    },
    {
      id: 3,
      name: "scribe-3",
      gender: "M",
      qualification: "BCom",
      exam: "BSc",
      examDate: "03-06-2021 10:30",
      duration: "1 Hr 30 Min",
      language: "English",
      venue: "Address-1",
    },
  ];

  const icons = {
    subTitle: "pencil-outline",
    examDate: "calendar",
    duration: "av-timer",
    language: "alpha",
    venue: "map-marker-outline",
  };

  const examDetails = ({ examDate, duration, language, venue }) => ({
    examDate,
    duration,
    language,
    venue,
  });

  const actions = [
    {
      color: colors.danger,
      name: "cancel",
      action: () => {
        console.log("Cancel action is called.");
      },
    },
  ];

  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <RequestCardComponent
            actions={actions}
            title={item.name + ", " + item.qualification}
            subTitle={item.exam}
            icons={icons}
            details={examDetails(item)}
            thumbnailIcon={item.gender === "M" ? "face" : "face-woman"}
            onPress={() => handleOnPress(item)}
          ></RequestCardComponent>
        )}
      ></FlatList>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default CandidateAppliedScreen;
