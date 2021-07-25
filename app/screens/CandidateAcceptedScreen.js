import React, { useEffect } from "react";
import { StyleSheet, FlatList } from "react-native";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import requestsApi from "../api/requests";
import requestStatus from "../config/requestStatus";
import gender from "../config/gender";
import useAuth from "../auth/useAuth";

function CandidateAcceptedScreen({ navigation }) {
  const { user } = useAuth();

  const {
    data: listing,
    error: listingError,
    loading: listingLoading,
    request: loadListings,
  } = useApi(requestsApi.findRequests);

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
      candidate: user.sub,
      requestStatus: requestStatus.ACCEPTED,
      startingFrom: new Date(),
    });
  };

  return (
    // TODO: Handle case when list is empty
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        onRefresh={callLoadListings}
        refreshing={listingLoading}
        renderItem={({ item }) => (
          <RequestCardComponent
            title={
              item.scribeResource.firstName +
              ", " +
              item.scribeResource.lastName
            }
            subTitle={item.examResource.name}
            icons={icons}
            details={examDetails(item.examResource)}
            thumbnailIcon={
              item.scribeResource.gender === gender.MALE ? "face" : "face-woman"
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

export default CandidateAcceptedScreen;
