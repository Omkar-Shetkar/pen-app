import React from "react";
import { View, StyleSheet, FlatList } from "react-native";
import SearchResultsCardComponent from "../components/SearchResultsCardComponent";
import Screen from "../components/Screen";
import gender from "../config/gender";
import requestsApi from "../api/requests";

function ScribeSearchResultsScreen({ route }) {
  const listing = route.params.data;

  const handleOnPress = async (scribe) => {
    console.log("Send request to ", scribe.firstName);
    const requestUpdate = {
      scribeId: scribe.id,
      ...route.params.requestUpdate,
    };
    console.log("requestUpdate ", requestUpdate);
    const result = await requestsApi.createRequest(requestUpdate);
    console.log("New Request ", result);
  };

  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <SearchResultsCardComponent
            title={item.firstName + " " + item.lastName}
            subTitle={item.email}
            thumbnailIcon={item.gender === gender.MALE ? "face" : "face-woman"}
            onPress={() => handleOnPress(item)}
          ></SearchResultsCardComponent>
        )}
      />
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default ScribeSearchResultsScreen;
