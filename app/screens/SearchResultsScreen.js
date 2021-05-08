import React from "react";
import { View, StyleSheet, FlatList } from "react-native";
import CardComponent from "../components/CardComponent";
import Screen from "../components/Screen";

function SearchResultsScreen(props) {
  const listing = [
    { id: 1, name: "candidate-1", qualification: "MCA", gender: "M" },
    { id: 2, name: "candidate-2", qualification: "BSc", gender: "F" },
    { id: 3, name: "candidate-3", qualification: "Phd", gender: "M" },
  ];
  return (
    <Screen>
      <FlatList
        data={listing}
        keyExtractor={(listing) => listing.id.toString()}
        renderItem={({ item }) => (
          <CardComponent
            title={item.name}
            subTitle={item.qualification}
            thumbnailIcon={item.gender === "M" ? "face" : "face-woman"}
          ></CardComponent>
        )}
      ></FlatList>
    </Screen>
  );
}

const styles = StyleSheet.create({
  container: {},
});

export default SearchResultsScreen;
