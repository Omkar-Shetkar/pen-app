import React, { useEffect } from "react";
import { StyleSheet, FlatList } from "react-native";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import colors from "../config/colors";
import requestStatus from "../config/requestStatus";
import requestsApi from "../api/requests";
import gender from "../config/gender";
import useAuth from "../auth/useAuth";

function ScribeReceivedScreen(props) {
  const { user } = useAuth();
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
      scribe: user.sub,
      requestStatus: requestStatus.PENDING,
      startingFrom: new Date(),
    });
  };

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
        name: "accept",
        action: async () => {
          const result = await requestsApi.updateRequest(item.id, {
            requestStatus: requestStatus.ACCEPTED,
          });
          if (result.ok) {
            callLoadListings();
          }
          //TODO: Request may fail with BAD REQUEST if scribe accepts multiple requests for the same time slot
        },
      },
      {
        name: "reject",
        action: async () => {
          const result = await requestsApi.deleteRequest(item.id);
          if (result.ok) {
            callLoadListings();
          }
        },
        color: colors.secondary,
      },
    ];
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
            actions={actions(item)}
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
