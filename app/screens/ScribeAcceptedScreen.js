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
import strings from "../config/strings";
import Toast from "react-native-toast-message";

function ScribeAcceptedScreen({ navigation }) {
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

  const callLoadListings = () => {
    loadListings({
      scribe: user.sub,
      requestStatus: requestStatus.ACCEPTED,
      startingFrom: new Date(),
    });
  };

  const actions = (item) => {
    return [
      {
        color: colors.danger,
        name: "decline",
        action: async () => {
          const result = await requestsApi.updateRequest(item.id, {
            requestStatus: requestStatus.REJECTED,
          });
          if (result.ok) {
            Toast.show({
              type: "success",
              text1: strings.request.decline,
            });
            callLoadListings();
          } else {
            Toast.show({
              type: "error",
              text1: strings.request.failed,
            });
          }
        },
      },
    ];
  };

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

  return (
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

export default ScribeAcceptedScreen;
