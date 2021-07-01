import React from "react";
import { StyleSheet, FlatList } from "react-native";
import RequestCardComponent from "../components/RequestCardComponent";

import Screen from "../components/Screen";
import colors from "../config/colors";

function CandidateAcceptedScreen(props) {
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

  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <RequestCardComponent
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

export default CandidateAcceptedScreen;
