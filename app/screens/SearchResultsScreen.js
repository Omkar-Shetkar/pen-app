import React from "react";
import { View, StyleSheet, FlatList } from "react-native";
import SearchResultsCardComponent from "../components/SearchResultsCardComponent";
import Screen from "../components/Screen";

function SearchResultsScreen(props) {
  const listing = [
    { id: 1, name: "scribe-1", qualification: "MCA", gender: "M" },
    { id: 2, name: "scribe-2", qualification: "BSc", gender: "F" },
    { id: 3, name: "scribe-3", qualification: "Phd", gender: "M" },
    { id: 4, name: "scribe-4", qualification: "MCA", gender: "F" },
    { id: 5, name: "scribe-5", qualification: "PUC", gender: "M" },
    { id: 6, name: "scribe-6", qualification: "MA", gender: "F" },
    { id: 7, name: "scribe-7", qualification: "MCA", gender: "M" },
    { id: 8, name: "scribe-8", qualification: "MSc", gender: "F" },
  ];

  const handleOnPress = (scribe) => {
    console.log("Send request to ", scribe.name);
  };

  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <SearchResultsCardComponent
            title={item.name}
            subTitle={item.qualification}
            thumbnailIcon={item.gender === "M" ? "face" : "face-woman"}
            onPress={() => handleOnPress(item)}
          ></SearchResultsCardComponent>
        )}
      ></FlatList>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default SearchResultsScreen;
