import React, { useEffect } from "react";
import { StyleSheet, FlatList, View } from "react-native";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import AppText from "../components/AppText";
import requestsApi from "../api/requests";
import requestStatus from "../config/requestStatus";
import gender from "../config/gender";
import useAuth from "../auth/useAuth";
import strings from "../config/strings";
import colors from "../config/colors";

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

  const examDetails = ({ start, end, language, address }, { mobile }) => {
    start = moment(start).format("MMMM Do, h:mm A");
    end = moment(end).format("MMMM Do, h:mm A");
    return {
      start,
      end,
      language,
      address,
      mobile,
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
    <Screen>
      {listing && listing.length == 0 && (
        <View
          style={styles.emptyContainer}
          accessible={true}
          accessibilityLabel={strings.request.noData}
        >
          <AppText style={styles.noData}>{strings.request.noData}</AppText>
        </View>
      )}
      {listing && listing.length > 0 && (
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
              details={examDetails(item.examResource, item.scribeResource)}
              thumbnailIcon={
                item.scribeResource.gender === gender.MALE ? "male" : "female"
              }
              onPress={() => handleOnPress(item)}
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

export default CandidateAcceptedScreen;
