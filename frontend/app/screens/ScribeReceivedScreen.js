import React, { useEffect } from "react";
import { StyleSheet, FlatList, View, Alert } from "react-native";
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
import AppText from "../components/AppText";

function ScribeReceivedScreen({ navigation }) {
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
      requestStatus: requestStatus.PENDING,
      startingFrom: new Date(),
    });
  };

  const examDetails = ({ start, end, language, address, city }) => {
    start = moment(start).format("MMMM Do, h:mm A");
    end = moment(end).format("MMMM Do, h:mm A");
    return {
      start,
      end,
      language,
      address,
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
            Toast.show({
              type: "success",
              text1: strings.request.accept,
            });
            callLoadListings();
          } else {
            Toast.show({
              type: "error",
              text1: strings.request.failed,
            });
          } //TODO: Request may fail with BAD REQUEST if scribe accepts multiple requests for the same time slot
        },
      },
      {
        name: "reject",
        action: () => {
          Alert.alert("Reject", strings.request.rejectConfirm, [
            {
              text: "NO",
              onPress: () => console.log("NO Pressed"),
              style: "cancel",
            },
            {
              text: "YES",
              onPress: async () => {
                const result = await requestsApi.deleteRequest(item.id);
                if (result.ok) {
                  Toast.show({
                    type: "success",
                    text1: strings.request.reject,
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
          ]);
        },
        color: colors.secondary,
      },
    ];
  };

  return (
    <Screen>
      {listing && listing.length == 0 && (
        <View style={styles.emptyContainer}>
          <AppText style={styles.noData}>{strings.request.noData}</AppText>
        </View>
      )}
      {listing && listing.length > 0 && (
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
              details={examDetails(item.examResource)}
              thumbnailIcon={
                item.candidateResource.gender === gender.MALE
                  ? "male"
                  : "female"
              }
              onPress={() => {
                console.log("on press called...");
                handleOnPress(item);
              }}
            ></RequestCardComponent>
          )}
        ></FlatList>
      )}
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
  emptyContainer: { flex: 1, alignItems: "center", justifyContent: "center" },
  noData: { color: colors.medium },
});

export default ScribeReceivedScreen;
