import React, { useEffect } from "react";
import { StyleSheet, FlatList } from "react-native";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import examEventsApi from "../api/examEvents";
import examStatus from "../config/examStatus";
import gender from "../config/gender";
import useAuth from "../auth/useAuth";

function ScribeArchiveScreen({ navigation }) {
  const { user } = useAuth();
  const {
    data: listing,
    error: listingError,
    loading: listingLoading,
    request: loadListings,
  } = useApi(examEventsApi.findExamEvents);

  useEffect(() => {
    const tabPress = navigation.addListener("tabPress", (e) => {
      callLoadListings();
    });
    const focus = navigation.addListener("focus", (e) => {
      callLoadListings();
    });
  }, [navigation]);

  const icons = {
    subTitle: "pencil-outline",
    examDate: "calendar",
    duration: "av-timer",
    language: "alpha",
    address: "map-marker-outline",
  };

  const examDetails = ({ start, end, language, address }) => {
    start = moment(start).format("MMMM Do, h:mm A");
    end = moment(end).format("MMMM Do, h:mm A");
    return {
      start,
      end,
      language,
      address,
    };
  };

  const callLoadListings = () => {
    loadListings({
      scribe: user.sub,
      examStatus: examStatus.COMPLETED,
    });
  };

  return (
    // TODO: Handle case when list is empty
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(item) => item.id.toString()}
        onRefresh={callLoadListings}
        refreshing={listingLoading}
        renderItem={({ item }) => (
          <RequestCardComponent
            title={
              item.candidateResource.firstName +
              ", " +
              item.candidateResource.lastName
            }
            subTitle={item.examResource.name}
            icons={icons}
            details={examDetails(item.examResource)}
            thumbnailIcon={
              item.candidateResource.gender === gender.MALE
                ? "face"
                : "face-woman"
            }
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

export default ScribeArchiveScreen;
