import React, { useEffect } from "react";
import { StyleSheet, FlatList, View } from "react-native";
import moment from "moment";

import RequestCardComponent from "../components/RequestCardComponent";
import Screen from "../components/Screen";
import examEventsApi from "../api/examEvents";
import examStatus from "../config/examStatus";
import gender from "../config/gender";
import useAuth from "../auth/useAuth";
import AppText from "../components/AppText";
import colors from "../config/colors";
import strings from "../config/strings";

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

export default ScribeArchiveScreen;
