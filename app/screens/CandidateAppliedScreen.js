import React, { useEffect } from "react";
import { StyleSheet } from "react-native";
import { FlatList } from "react-native-gesture-handler";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import colors from "../config/colors";
import requestsApi from "../api/requests";
import gender from "../config/gender";

function CandidateAppliedScreen(props) {
  const {
    data: listing,
    error: listingError,
    loading: listingLoading,
    request: loadListings,
  } = useApi(requestsApi.findRequests);

  const {
    data: request,
    error: deleteError,
    loading: deleteLoading,
    request: deleteRequest,
  } = useApi(requestsApi.deleteRequest);

  useEffect(() => {
    loadListings({
      //TODO: Use actual candidate id
      candidate: 1,
      requestStatus: "PENDING",
      startingFrom: new Date(),
    });
  }, []);

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

  const actions = (item) => {
    return [
      {
        color: colors.danger,
        name: "cancel",
        action: () => {
          deleteRequest(item.id);
        },
      },
    ];
  };

  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(item) => item.id.toString()}
        renderItem={({ item }) => (
          <RequestCardComponent
            actions={actions(item)}
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

export default CandidateAppliedScreen;
