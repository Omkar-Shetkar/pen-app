import React, { useEffect } from "react";
import { StyleSheet, FlatList } from "react-native";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import colors from "../config/colors";
import requestStatus from "../config/requestStatus";
import requestsApi from "../api/requests";
import gender from "../config/gender";

function ScribeReceivedScreen(props) {
  const {
    data: listing,
    error: listingError,
    loading: listingLoading,
    request: loadListings,
  } = useApi(requestsApi.findRequests);

  useEffect(() => {
    callLoadListings();
  }, []);

  const callLoadListings = () => {
    loadListings({
      //TODO: Use actual candidate id
      scribe: 1,
      requestStatus: requestStatus.PENDING,
      startingFrom: new Date(),
    });
  };

  console.log("listing", listing);

  const icons = {
    subTitle: "pencil-outline",
    examDate: "calendar",
    duration: "av-timer",
    language: "alpha",
    venue: "map-marker-outline",
  };

  const examDetails = ({ start, end, language, venue }) => {
    start = moment(start).format("MMMM Do, h:mm A");
    end = moment(end).format("MMMM Do, h:mm A");
    return {
      start,
      end,
      language,
      venue,
    };
  };

  const actions = [
    {
      name: "accept",
      action: () => {
        console.log("Accept action is called.");
      },
    },
    {
      name: "reject",
      action: () => {
        console.log("Reject action is called.");
      },
      color: colors.secondary,
    },
  ];

  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(item) => item.id.toString()}
        onRefresh={callLoadListings}
        refreshing={listingLoading}
        renderItem={({ item }) => (
          <RequestCardComponent
            actions={actions}
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

export default ScribeReceivedScreen;
